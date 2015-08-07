package cn.luquba678.activity.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.MainFragmentActivity;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.utils.Until;

public class PersonSettingDialog extends FullScreenDialog implements
		View.OnClickListener {
	private Context context;
	private LinearLayout ll_feedback, ll_person_about, ll_share,person_question;
	private PersonSettingFeedBackDialog pFeedBackDialog;
	private PersonSettingAboutDialog pSettingAboutDialog;
    private PersonCommonMistakeDialog personCommonMistakeDialog;
	public PersonSettingDialog(Context context) {
		super(context);
		this.context = context;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_setting);
		initView();
		initTitle(findViewById(R.id.person_setting_title), "设置");
	}

	private void initView() {
		ll_feedback = (LinearLayout) findViewById(R.id.person_feedback);
		ll_feedback.setOnClickListener(this);
		ll_person_about = (LinearLayout) findViewById(R.id.person_about);
		ll_person_about.setOnClickListener(this);
		ll_share = (LinearLayout) findViewById(R.id.share_friend);
		ll_share.setOnClickListener(this);
		person_question = (LinearLayout) findViewById(R.id.person_question);
		person_question.setOnClickListener(this);
	}

	@Override
	public void initTitle(View view, String title) {
		super.initTitle(view, title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_feedback:
			pFeedBackDialog = new PersonSettingFeedBackDialog(context);
			pFeedBackDialog.show();
			break;
		case R.id.person_about:
			pSettingAboutDialog = new PersonSettingAboutDialog(context);
			pSettingAboutDialog.show();
			break;
		case R.id.share_friend:
			Until.showShare(context, mHandler,"录取吧","http://120.26.112.250/apk/luquba.apk","http://120.26.112.250/apk/678icon.jpg");
            break;
		case R.id.person_question:
			personCommonMistakeDialog = new PersonCommonMistakeDialog(context);
			personCommonMistakeDialog.show();
		default:
			break;
		}
	}

}
