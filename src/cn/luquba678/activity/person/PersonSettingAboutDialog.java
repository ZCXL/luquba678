package cn.luquba678.activity.person;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuchao.bean.Version;
import com.zhuchao.function.CheckVersion;
import com.zhuchao.utils.FileSystem;
import com.zhuchao.utils.ImageProcess;

import cn.luquba678.R;
import cn.luquba678.activity.MainFragmentActivity;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.utils.ToolUtils;

public class PersonSettingAboutDialog extends FullScreenDialog implements
		View.OnClickListener {
	private LinearLayout about_us, ll_person_about,clear_cache;
	private RelativeLayout check_version;
	private Context context;
    private PersonSettingAboutUsDialog personSettingAboutUsDialog;

	private CheckVersion checkVersion;

	private String versionString;
	private TextView version_textview;

	private Version version;
	/**
	 *init
	 * @param context
	 */
	public PersonSettingAboutDialog(Context context) {
		super(context);
		this.context = context;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 1:
					ToolUtils.showShortToast(context, "缓存已清除");
					break;
				case 2:
					if(version!=null&&!versionString.equals(version.getVersionId())){
						new AlertDialog.Builder(context).setTitle("New Version").setMessage("VersionId:"+version.getVersionId()+"\nVersionDescription:"+version.getVersionDescription()).setPositiveButton("Update now", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(MainFragmentActivity.downloadService!=null)
									MainFragmentActivity.downloadService.addDownloadTask(version.getVersionUrl(), 1);
							}
						}).setNegativeButton("Remember me later", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();
					}
					break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_setting_about);
		initView();
		initTitle(findViewById(R.id.person_setting_title), "关于软件");
	}

	private void initView() {
		about_us = (LinearLayout) findViewById(R.id.about_us);
		about_us.setOnClickListener(this);
		clear_cache = (LinearLayout) findViewById(R.id.clear_cache);
		clear_cache.setOnClickListener(this);

		check_version=(RelativeLayout)findViewById(R.id.check_update);
		check_version.setOnClickListener(this);

		version_textview=(TextView)findViewById(R.id.version_number);
		//set current version
		PackageManager packageManager=context.getPackageManager();
		PackageInfo packageInfo;
		try {
			packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionString=packageInfo.versionName;
			version_textview.setText("当前版本：" + versionString);
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * use check version interface to get versions
		 */
		checkVersion=new CheckVersion(context);
		checkVersion.setOnVersionCheckListener(new CheckVersion.OnVersionCheckListener() {
			@Override
			public void getVersion(Version version) {
				Log.d("zhuchao",version.getVersionUrl());
				PersonSettingAboutDialog.this.version=version;
				mHandler.sendEmptyMessage(2);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.about_us:
				personSettingAboutUsDialog = new PersonSettingAboutUsDialog(context);
				personSettingAboutUsDialog.show();
				break;
			case R.id.clear_cache:
				showClearCacheDialog();
				break;
			case R.id.btn_cancle:
				clearCacheDialog.dismiss();
				break;
			case R.id.btn_ok:
				ImageProcess.DeleteImage();
				mHandler.sendEmptyMessage(1);
				clearCacheDialog.dismiss();
				break;
			case R.id.check_update:
				checkVersion.startCheck();
				break;
			default:
				break;
		}
	}
	AlertDialog clearCacheDialog;

	private void showClearCacheDialog() {
		clearCacheDialog = new AlertDialog.Builder(context).create();
		clearCacheDialog.show();
		clearCacheDialog.getWindow().setContentView(R.layout.person_clearcache_dialog);
		Button btn_cancle, btn_ok;
		btn_cancle = (Button) clearCacheDialog.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(this);
		btn_ok = (Button) clearCacheDialog.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
	}

}
