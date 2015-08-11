package cn.luquba678.activity.person;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.ToolUtils;

public class PersonSettingFeedBackDialog extends FullScreenDialog implements
		android.view.View.OnClickListener {
	private Context context;
	private EditText ed_feedback;
	private Button ed_commit_btn;

	public PersonSettingFeedBackDialog(Context context) {
		super(context);
		this.context = context;
	}
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				ToolUtils.showShortToast(context, "意见反馈成功..");
				dismiss();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_setting_feedback);
		initTitle(findViewById(R.id.person_setting_feedback_title), "意见反馈");
		ed_feedback = (EditText) findViewById(R.id.ed_feedback);
		ed_commit_btn = (Button) findViewById(R.id.ed_commit_btn);
		ed_commit_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ed_commit_btn:
			if (ed_feedback.getText().toString().trim().isEmpty()) {
				ToolUtils.showShortToast(context, "反馈意见内容不能为空!!");
				return;
			}
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					
				}});
			final MultipartEntity entity = new MultipartEntity();
			String content = ed_feedback.getText().toString();
			final String feedBackUrl = Const.FEED_BACK+"?uid="+User.getUID(context) +"&login_token="+User.getLoginToken(context);
			try {
				entity.addPart("content", new StringBody(content));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					try {
						JSONObject obj = HttpUtil.getRequestJson(feedBackUrl, entity);
						Integer errcode = obj.getInteger("errcode");
						if(errcode == 0){
							Message msg = handler.obtainMessage();
							msg.what = 1;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				
			});
			break;

		default:
			break;
		}
	}
}
