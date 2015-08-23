package cn.luquba678.activity.person;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuchao.bean.Version;
import com.zhuchao.function.CheckVersion;
import com.zhuchao.view_rewrite.AboutUs;
import com.zhuchao.view_rewrite.ClearCacheDialog;
import com.zhuchao.view_rewrite.VersionCheckDialog;

import cn.luquba678.R;
import cn.luquba678.activity.AgreementDialog;
import cn.luquba678.ui.FullScreenDialog;

public class PersonSettingAboutDialog extends FullScreenDialog implements View.OnClickListener {
	private LinearLayout about_us,clear_cache,rule;
	private RelativeLayout check_version;
	private Context context;
    private AboutUs aboutUs;

	private CheckVersion checkVersion;

	private String versionString;
	private TextView version_textview;

	private Version version;
	private ImageView back_image;
	private LinearLayout back;
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

					break;
				case 2:
					if(!version.getVersionId().equals(versionString))
						new VersionCheckDialog(getContext(),version.getVersionId(),version.getVersionDescription(),version.getVersionUrl()).show();
					else
						Toast.makeText(getContext(),"已是最新版本",Toast.LENGTH_SHORT).show();
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
	}

	private void initView() {
		aboutUs=new AboutUs(context);
		back_image=(ImageView)findViewById(R.id.title_top_image);
		back=(LinearLayout)findViewById(R.id.top_back);
		back_image.setOnClickListener(this);
		back.setOnClickListener(this);
		about_us = (LinearLayout) findViewById(R.id.about_us);
		about_us.setOnClickListener(this);
		clear_cache = (LinearLayout) findViewById(R.id.clear_cache);
		clear_cache.setOnClickListener(this);
		rule=(LinearLayout)findViewById(R.id.use_detail);
		rule.setOnClickListener(this);

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
				aboutUs.show();
				break;
			case R.id.clear_cache:
				new ClearCacheDialog(getContext()).show();
				break;
			case R.id.check_update:
				checkVersion.startCheck();
				break;
			case R.id.use_detail:
				AgreementDialog agreementDialog = new AgreementDialog(context);
				agreementDialog.show();
				break;
			case R.id.title_top_image:
				dismiss();
				break;
			case R.id.top_back:
				dismiss();
				break;
			default:
				break;
		}
	}

}
