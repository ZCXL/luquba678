package cn.luquba678.activity;

import com.alibaba.fastjson.JSONObject;
import com.zhuchao.http.Network;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;
import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.ui.HttpUtil;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPassActivity1 extends CommonActivity implements TextWatcher {

	private Button nextButton;
	private boolean flag = false;
	private AlertDialog alertDialog;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					toast("发送验证码成功");
					finish();
					break;
				case 1:
					toast("发送验证码成功失败,请重试");
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}

	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;
		case R.id.btn_ok:
			alertDialog.dismiss();
			if(Network.checkNetWorkState(this)) {
				try {
					MultipartEntity entity = new MultipartEntity();
					String tel = phoneNumber.getText().toString();
					entity.addPart("tel", new StringBody(tel));
					String json = HttpUtil.postRequestEntity(Const.FORGETPASS_GETMSG_URL, entity);
					JSONObject obj = JSONObject.parseObject(json);
					Integer errcode = obj.getInteger("errcode");
					if (errcode == 0) {
						Intent intent = new Intent(this, ResetPassActivity2.class);
						intent.putExtra("tel", tel);
						this.startActivity(intent);
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(1);
					e.printStackTrace();
				}
			}else{
				Toast.makeText(this, "未连接网络", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_cancel:
			alertDialog.dismiss();
			break;
		case R.id.top_submit:
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.show();
			alertDialog.getWindow().setContentView(R.layout.alert_ensure);
			alertDialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(this);
			alertDialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(this);
			TextView hint = (TextView) alertDialog.getWindow().findViewById(R.id.hint);
			hint.setText(phoneNumber.getText());

			break;
		default:
			break;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() == 11 && !flag) {
			flag = true;
			nextButton.setEnabled(true);
			nextButton.setTextColor(0xFFFFFFFF);
			Log.e("监听内容", "已有输入，可以发布");
		} else if (s.length() != 11 && flag) {
			flag = false;
			nextButton.setEnabled(false);
			nextButton.setTextColor(0xFFCFCBCB);
			Log.e("监听内容", "没有输入，不可以发布");
		}
	}

	EditText phoneNumber;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_reset_pass1);
		setOnClickLinstener(R.id.top_back, R.id.top_submit);
		((TextView) findViewById(R.id.top_text)).setText("重置密码");

		nextButton = getView(R.id.top_submit);
		phoneNumber = getView(R.id.the_phone_number);
		nextButton.setText("下一步");
		nextButton.setEnabled(false);
		nextButton.setTextColor(0xFFCFCBCB);
		nextButton.setOnClickListener(this);
		phoneNumber.addTextChangedListener(this);

	}

}
