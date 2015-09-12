package cn.luquba678.activity;

import com.alibaba.fastjson.JSONObject;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;
import cn.luquba678.R;
import cn.luquba678.activity.listener.TextLenthWatcher;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ResetPassActivity2 extends CommonActivity {

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.top_back:
				this.finish();
				break;
			case R.id.do_resetpass:
				doResetpass();
				break;
			case R.id.resend:
				alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.show();
				alertDialog.getWindow().setContentView(R.layout.alert_ensure);
				alertDialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(this);
				alertDialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(this);
				TextView hint = (TextView) alertDialog.getWindow().findViewById(R.id.hint);
				hint.setText(tel);
				break;
			case R.id.btn_ok:
				try {
					MultipartEntity entity = new MultipartEntity();
					entity.addPart("tel", new StringBody(tel));
					String json = HttpUtil.postRequestEntity(Const.FORGETPASS_GETMSG_URL, entity);
					JSONObject obj = JSONObject.parseObject(json);
					Integer errcode = obj.getInteger("errcode");
					if (errcode == 0) {
						Toast.makeText(ResetPassActivity2.this,"已发送",Toast.LENGTH_SHORT).show();
						count=60;
						resend.setBackgroundResource(R.drawable.bg_btn_forbid_click);
						resend.setText("60秒后重发");
						resend.setClickable(false);
						resend.setEnabled(false);
						resend.setFocusable(false);
						timer=new Timer();
						mTimerTask=new MyTimerTask();
						timer.schedule(mTimerTask, 0, 1000);
					} else {
						toast(obj.getString("errmsg"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				alertDialog.dismiss();
				break;
			case R.id.btn_cancel:
				alertDialog.dismiss();
				break;
			default:
				break;
		}
	}

	private Handler handler=new Handler() {
		public void handleMessage (Message msg){
			switch (msg.what) {
				case 0:
					if(count>0) {
						count--;
						resend.setText(String.valueOf(count)+"秒后重发");
					}else{
						timer.cancel();
						resend.setBackgroundResource(R.drawable.frame_radius5_alfa0_main_color);
						resend.setText("重发验证码", TextView.BufferType.NORMAL);
						resend.setClickable(true);
						resend.setEnabled(true);
						resend.setFocusable(true);
					}
					break;
			}
		}
	};
	private void doResetpass() {
		String password = et_pass.getText().toString();
		String repassword = et_repass.getText().toString();
		String verify = et_verify.getText().toString();
		if (!password.equals(repassword)) {
			new AlertDialog.Builder(this).setMessage("两次密码不一致！")
					.setPositiveButton("确定", null).show();
			return;
		}
		if (verify.length() < 4) {
			new AlertDialog.Builder(this).setMessage("验证码输入错误！")
					.setPositiveButton("确定", null).show();
			return;
		}
		try {
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("tel", new StringBody(tel));
			entity.addPart("password", new StringBody(password));
			entity.addPart("sms", new StringBody(verify));
			String json = HttpUtil.postRequestEntity(Const.FORGETPASS_URL,
					entity);
			JSONObject obj = JSONObject.parseObject(json);
			Integer errcode = obj.getInteger("errcode");
			if (errcode == 0) {
				SharedPreferences sharedPreferences = getSharedPreferences("luquba_login", Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();// 获取编辑器
				editor.putString(User.TEL, tel);
				editor.putString(User.PASSWORD, password);
				editor.commit();// 提交修改
				Toast.makeText(ResetPassActivity2.this,"修改密码成功",Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(this, LoginActivity.class);
				this.startActivity(intent);
				finish();
			} else {
				toast(obj.getString("errmsg"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private EditText et_verify, et_pass, et_repass;
	private Button do_resetpass;
	private String tel;
	private Button resend;
	private AlertDialog alertDialog;
	private Timer timer;
	private int count=60;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_reset_pass2);
		setOnClickLinstener(R.id.top_back, R.id.do_resetpass);
		((TextView) findViewById(R.id.top_text)).setText("重置密码");
		do_resetpass = getView(R.id.do_resetpass);
		et_verify = getView(R.id.verify);
		et_pass = getView(R.id.password);
		et_repass = getView(R.id.repassword);
		do_resetpass.setEnabled(false);
		do_resetpass.setTextColor(0xFFCFCBCB);
		et_repass.addTextChangedListener(new TextLenthWatcher(do_resetpass, 6));
		tel = getIntent().getStringExtra("tel");
		resend=(Button)findViewById(R.id.resend);
		resend.setOnClickListener(this);
		resend.setText("60秒后重发");
		resend.setBackgroundResource(R.drawable.bg_btn_forbid_click);
		resend.setClickable(false);
		resend.setEnabled(false);
		resend.setFocusable(false);
		timer=new Timer();
		timer.schedule(mTimerTask,0,1000);
	}

	MyTimerTask mTimerTask=new MyTimerTask();
	public class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			handler.sendEmptyMessage(0);
		}
	}
}
