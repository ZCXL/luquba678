package cn.luquba678.activity.person;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhuchao.share.ShareInit;

import cn.luquba678.R;
import cn.luquba678.ui.FullScreenDialog;
public class PersonSettingDialog extends FullScreenDialog implements View.OnClickListener {
	private Context context;
	private RelativeLayout ll_feedback, ll_person_about, ll_share,person_question,ll_comment;
	private PersonSettingFeedBackDialog pFeedBackDialog;
	private PersonSettingAboutDialog pSettingAboutDialog;
    private PersonCommonMistakeDialog personCommonMistakeDialog;
	private ImageView back_image;
	private LinearLayout back;
	public PersonSettingDialog(Context context) {
		super(context);
		this.context = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_setting);
		ShareInit.initSDK(context);
		initView();
	}

	private void initView() {
		ll_feedback = (RelativeLayout) findViewById(R.id.person_feedback);
		ll_feedback.setOnClickListener(this);
		ll_person_about = (RelativeLayout) findViewById(R.id.person_about);
		ll_person_about.setOnClickListener(this);
		ll_share = (RelativeLayout) findViewById(R.id.share_friend);
		ll_share.setOnClickListener(this);
		person_question = (RelativeLayout) findViewById(R.id.person_question);
		person_question.setOnClickListener(this);
		ll_comment=(RelativeLayout)findViewById(R.id.person_account_line);
		ll_comment.setOnClickListener(this);

		back_image=(ImageView)findViewById(R.id.title_top_image);
		back_image.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.top_back);
		back.setOnClickListener(this);
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
				ShareInit.showShare(false, null, context, "录取吧", "http://120.26.112.250/apk/luquba.apk", "[录取吧]为你开启大学之门\nhttp://120.26.112.250/apk/luquba.apk", "http://120.26.112.250/apk/678icon.jpg");
				break;
			case R.id.person_question:
				personCommonMistakeDialog = new PersonCommonMistakeDialog(context);
				personCommonMistakeDialog.show();
				break;
			case R.id.person_account_line:
				Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
				Intent intent = new Intent(Intent.ACTION_VIEW,uri);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
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
