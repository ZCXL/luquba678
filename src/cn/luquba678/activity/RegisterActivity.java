package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView.BufferType;
import android.widget.Toast;

import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.MD5;
import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;

public class RegisterActivity extends CommonActivity implements TextWatcher {
	private File headImageFile;

	private Button goRegist, btn_get_verify;
	private EditText et_usernick;

	private EditText et_usertel;
	private Button btn_register;

	private String imageName;
	private EditText et_tel, et_password, et_repassword, et_verify;
	private String tel;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		((TextView) findViewById(R.id.top_text)).setText("注册");
		setOnClickLinstener(R.id.agree, R.id.top_back, R.id.do_regist,
				R.id.get_verify);
		btn_get_verify = getView(R.id.get_verify);
		goRegist = getView(R.id.do_regist);
		et_tel = getView(R.id.regist_phone_number);
		et_password = getView(R.id.password);
		et_repassword = getView(R.id.repassword);
		et_verify = getView(R.id.verify);
		et_tel.addTextChangedListener(this);
		et_password.addTextChangedListener(this);
		dialog = new ProgressDialog(RegisterActivity.this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;
		case R.id.do_regist:
			doRegist();
			break;
		case R.id.get_verify:
			getVerify();
			break;
		case R.id.agree:
			AgreementDialog agreementDialog = new AgreementDialog(self);
			agreementDialog.show();
			break;
		default:
			break;
		}
	}

	public static final String WAITHINT = "秒后重发";
	private int countDown;
	Runnable updateThread = new Runnable() {

		@Override
		public void run() {
			// 获取消息
			Message msg = updateHandler.obtainMessage();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			msg.what = --countDown;
			updateHandler.sendMessage(msg);
		}
	};
	boolean countFlag = true;
	Handler updateHandler = new Handler() {
		// 把消息从消息队列中取出处理
		public void handleMessage(Message msg) {
			if (msg.what < 0) {
				btn_get_verify.setBackgroundResource(R.drawable.frame_radius5_alfa0_main_color);
				btn_get_verify.setEnabled(true);
				btn_get_verify.setText(R.string.get_verify, BufferType.NORMAL);
			} else {
				btn_get_verify.setText(msg.what + WAITHINT);
				ste.execute(updateThread);
			}

		}
	};
	private static ExecutorService ste;

	private void getVerify() {

		tel = et_tel.getText().toString();
		final String get = Const.GET_VRIFY_URL;

		if (tel.length() == 11) {

			// + "?api_token="+ Const.VRIFY_KEY + "&tel=" + tel;
			Log.i("短信请求", get);
			MultipartEntity entity = new MultipartEntity();
			try {
				entity.addPart("tel", new StringBody(tel));

				// entity.addPart("api_token",new
				// StringBody("ee8d24c15bc04befa12ff717734d5344"));
				JSONObject jsonObject = HttpUtil.getRequestJson(get, entity);

				int code = jsonObject.getInteger("errcode");
				switch (code) {
				case 0:
					countDown = 60;
					ste = Executors.newSingleThreadExecutor();
					btn_get_verify
							.setBackgroundResource(R.drawable.bg_btn_forbid_click);
					btn_get_verify.setEnabled(false);
					btn_get_verify.setText(60 + WAITHINT);
					ste.execute(updateThread);
					break;

				/*
				 * case 1:toast("验证码过期!"); break; case 2:toast("用户已注册!"); break;
				 * case 3: break; case 4: break;
				 */
				default:
					String errmsg = jsonObject.getString("errmsg");
					toast(errmsg);
					break;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			toast("请输入正确的手机号");
		}
	}

	private void doRegist() {
		countFlag = false;
		tel = et_tel.getText().toString();
		String password = et_password.getText().toString();
		String repassword = et_repassword.getText().toString();
		String verify = et_verify.getText().toString();

		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(tel)
				|| StringUtils.isEmpty(repassword)
				|| StringUtils.isEmpty(verify)) {
			return;
		}

		if (verify.length() != 6) {
			new AlertDialog.Builder(this).setMessage("验证码输入错误！")
					.setPositiveButton("确定", null).show();
			return;
		}

		if (password.length() < 6) {
			new AlertDialog.Builder(this).setMessage("密码不能少于6位！")
					.setPositiveButton("确定", null).show();
			return;
		}

		if (!password.equals(repassword)) {
			new AlertDialog.Builder(this).setMessage("两次密码不一致！")
					.setPositiveButton("确定", null).show();
			return;
		}

		try {
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("password", new StringBody(password));
			entity.addPart("sms", new StringBody(verify));
			entity.addPart("tel", new StringBody(tel));
			JSONObject obj = HttpUtil.getRequestJson(Const.REGIST_URL, entity);
			int errcode = obj.getIntValue("errcode");
			if (errcode == 0) {
				Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
				RegisterActivity.this.finish();
			} else {
				toast(obj.getString("errmsg"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 提交注册
	 */
	private void submitRegist() {
		dialog.setMessage("正在注册...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		String usernick = et_usernick.getText().toString().trim();
		final String userpwd = et_password.getText().toString().trim();
		String user_phone = et_usertel.getText().toString().trim();
		Map<String, Object> map = new HashMap<String, Object>();

		if (headImageFile != null && headImageFile.exists()) {
			map.put("file", headImageFile.toString());
			map.put("image", imageName);
		} else {
			map.put("image", "false");
		}
		map.put("user_nickname", usernick);
		map.put("user_phone", user_phone);
		map.put("userpwd", MD5.MD5Encode(userpwd));
	}

	class TextChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count) {

			boolean Sign1 = et_usernick.getText().length() > 0;
			boolean Sign2 = et_usertel.getText().length() > 0;
			boolean Sign3 = et_password.getText().length() > 0;

			if (Sign1 & Sign2 & Sign3) {
				btn_register.setTextColor(0xFFFFFFFF);
				btn_register.setEnabled(true);
			}
			// 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
			else {
				btn_register.setTextColor(0xFFD0EFC6);
				btn_register.setEnabled(false);
			}
		}

	}
	@SuppressLint("SdCardPath")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode,resultCode,data);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	/**
	 * 剪裁图片
	 * 
	 * @param uri
	 * @param size
	 */
	/*
	 * @SuppressLint("SdCardPath") private File startPhotoZoom(Uri uri, int
	 * size) { File cutFile = new File(CacheUtil.getCache(), "cut_"+imageName);
	 * Intent intent = PictureUtils.ZoomPhoto(uri, cutFile, 1, 480); //
	 * crop为true是设置在开启的intent中设置显示的view可以剪裁 startActivityForResult(intent,
	 * PHOTO_REQUEST_CUT); return cutFile; }
	 * 
	 * public void register(final String hxid, final String password) { if
	 * (!TextUtils.isEmpty(hxid) && !TextUtils.isEmpty(password)) { new
	 * Thread(new Runnable() { public void run() { try { // 调用sdk注册方法
	 * EMChatManager.getInstance().createAccountOnServer(hxid, password);
	 * runOnUiThread(new Runnable() { public void run() { if
	 * (!RegisterActivity.this.isFinishing()) dialog.dismiss(); // 保存用户名 //
	 * DemoApplication.getInstance().setUserName(hxid);
	 * 
	 * Toast.makeText(getApplicationContext(), "注册成功", 0).show(); finish(); }
	 * }); } catch (final EaseMobException e) { runOnUiThread(new Runnable() {
	 * public void run() { if (!RegisterActivity.this.isFinishing())
	 * dialog.dismiss(); int errorCode = e.getErrorCode(); if (errorCode ==
	 * EMError.NONETWORK_ERROR) { Toast.makeText(getApplicationContext(),
	 * "网络错误", Toast.LENGTH_SHORT).show(); } else if (errorCode ==
	 * EMError.USER_ALREADY_EXISTS) { Toast.makeText(getApplicationContext(),
	 * "用户名已存在", Toast.LENGTH_SHORT) .show(); } else if (errorCode ==
	 * EMError.UNAUTHORIZED) { Toast.makeText(getApplicationContext(), "失败",
	 * Toast.LENGTH_SHORT).show(); } else {
	 * Toast.makeText(getApplicationContext(), "用户名在" + e.getMessage(),
	 * Toast.LENGTH_SHORT).show(); } } }); } } }).start();
	 * 
	 * } }
	 *//**
	 * 发送验证码
	 */
	/*
	 * private void checkIdentifyingCode() { if
	 * (ToolUtils.isMobileNO(phone.getText().toString()) == false) { new
	 * AlertDialog.Builder(this) .setIcon( getResources().getDrawable(
	 * R.drawable.login_error_icon))
	 * .setTitle("获取验证码失败").setPositiveButton("确定", null)
	 * .setMessage("号码输入有误\n请检查后重新输入！").create().show(); } else { String str =
	 * ""; for (int i = 1; i <= 4; i++) { char ca = (char) (Math.random() * 9 +
	 * 48); str += ca + ""; } checkcode = str; Toast.makeText(this, str,
	 * Toast.LENGTH_LONG).show();// 产生随机数 } }
	 * 
	 * @Override public void onCheckedChanged(CompoundButton buttonView, boolean
	 * isChecked) { if (isChecked) {
	 * et_password.setTransformationMethod(HideReturnsTransformationMethod
	 * .getInstance()); } else {
	 * et_password.setTransformationMethod(PasswordTransformationMethod
	 * .getInstance());
	 * 
	 * } CharSequence charSequence = et_password.getText(); if (charSequence
	 * instanceof Spannable) { Spannable spanText = (Spannable) charSequence;
	 * Selection.setSelection(spanText, charSequence.length()); } }
	 */

}
