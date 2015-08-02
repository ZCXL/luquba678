package cn.luquba678.activity.person;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import cn.luquba678.R;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.utils.ToolUtils;
import cn.luquba678.utils.Until;

public class PersonSettingAboutDialog extends FullScreenDialog implements
		View.OnClickListener {
	private LinearLayout about_us, ll_person_about,clear_cache;
	private Context context;
    private PersonSettingAboutUsDialog personSettingAboutUsDialog;
	public PersonSettingAboutDialog(Context context) {
		super(context);
		this.context = context;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				ToolUtils.showShortToast(context, "缓存已清除");
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
			clearChacheDialog.dismiss();
			break;
		case R.id.btn_ok:
			mHandler.sendEmptyMessage(1);
			clearChacheDialog.dismiss();
			break;
		default:
			break;
		}
	}
	AlertDialog clearChacheDialog;

	private void showClearCacheDialog() {
		clearChacheDialog = new AlertDialog.Builder(context).create();
		clearChacheDialog.show();
		clearChacheDialog.getWindow().setContentView(R.layout.person_clearcache_dialog);
		Button btn_cancle, btn_ok;
		btn_cancle = (Button) clearChacheDialog.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(this);
		btn_ok = (Button) clearChacheDialog.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
	}

}
