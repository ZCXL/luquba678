package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.ContactsContract.DataUsageFeedback;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.DateUtils;
import cn.luquba678.utils.MD5;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;

public class RegisterActivity extends CommonActivity implements TextWatcher {
	private Uri headImageUri;
	private File headImageFile;

	private TextView agreement;
	private EditText phone, check_code;
	private Button register_next, register_reback, checkbtn, goRegist,
			btn_get_verify;
	private EditText et_usernick;

	private EditText et_usertel;
	private Button btn_register;
	private TextView tv_xieyi;
	private ImageView iv_hide;
	private ImageView iv_show;
	private ImageView iv_back;
	private ImageView iv_photo;

	private String imageName;
	private AlertDialog dlg;
	private PasswordTransformationMethod ptm;
	private EditText et_tel, et_password, et_repassword, et_verify;
	private String tel;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

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

		/*
		 * et_usernick = (EditText) findViewById(R.id.et_usernick);
		 * 
		 * et_usertel = (EditText) findViewById(R.id.et_usertel); et_password =
		 * (EditText) findViewById(R.id.et_password); // 监听多个输入框
		 * et_usernick.addTextChangedListener(new TextChange());
		 * et_usertel.addTextChangedListener(new TextChange());
		 * et_password.addTextChangedListener(new TextChange()); btn_register =
		 * (Button) findViewById(R.id.btn_register);
		 * btn_register.setOnClickListener(this); iv_back = (ImageView)
		 * findViewById(R.id.iv_back); iv_back.setOnClickListener(this);
		 * ((CheckBox) findViewById(R.id.show_pass))
		 * .setOnCheckedChangeListener(this); iv_photo = (ImageView)
		 * findViewById(R.id.iv_photo); iv_photo.setOnClickListener(this);
		 */
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
		/*
		 * case R.id.tv_content2: imageName = DateFormat.format("MMddHHmmssSS",
		 * new Date()) .toString() + ".jpg"; intent = new
		 * Intent(Intent.ACTION_PICK, null);
		 * intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		 * "image/*"); startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
		 * dlg.cancel(); break; case R.id.tv_content1: imageName =
		 * DateFormat.format("MMddHHmmssSS", new Date()) .toString() + ".jpg";
		 * headImageFile = ImageUtil.camara(imageName, this,
		 * PHOTO_REQUEST_TAKEPHOTO);
		 * 
		 * // 指定调用相机拍照后照片的储存路径 dlg.cancel(); break; case R.id.register_next: if
		 * ("".equals(phone.getText().toString()) ||
		 * "".equals(check_code.getText().toString())) // 判断 帐号和密码 { new
		 * AlertDialog.Builder(this) .setIcon( getResources().getDrawable(
		 * R.drawable.login_error_icon))
		 * .setTitle("亲，对不起").setPositiveButton("确定", null)
		 * .setMessage("手机号与验证码不能为空哦，\n请重试！").create().show(); break; } if
		 * (checkcode.equals(check_code.getText().toString()) && checkcode !=
		 * null) // 判断 帐号和密码 { Toast.makeText(getApplicationContext(), "登录成功",
		 * Toast.LENGTH_SHORT).show(); intent = new Intent();
		 * intent.setClass(this,
		 * com.sinzk.pkeggs.activities.registersteppw.class);
		 * startActivity(intent); Toast.makeText(getApplicationContext(),
		 * "登录成功", Toast.LENGTH_SHORT).show(); } else {
		 * 
		 * new AlertDialog.Builder(this) .setIcon( getResources().getDrawable(
		 * R.drawable.login_error_icon))
		 * .setTitle("验证失败").setPositiveButton("确定", null)
		 * .setMessage("帐号或者密码不正确，\n请检查后重新输入！").create().show(); } break; case
		 * R.id.btn_register: submitRegist(); break; case R.id.checkbtn:
		 * checkIdentifyingCode(); case R.id.iv_photo: showCamera(); break;
		 */

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
				btn_get_verify
						.setBackgroundResource(R.drawable.frame_radius5_alfa0_main_color);
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

			// entity.addPart("api_token",new
			// StringBody("ee8d24c15bc04befa12ff717734d5344"));
			JSONObject obj = HttpUtil.getRequestJson(Const.REGIST_URL, entity);
			int errcode = obj.getIntValue("errcode");
			if (errcode == 0) {

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

	/*
	 * private void showCamera() { dlg = new AlertDialog.Builder(this).create();
	 * dlg.show(); Window window = dlg.getWindow(); // *** 主要就是在这里实现这种效果的. //
	 * 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
	 * window.setContentView(R.layout.alertdialog); // 为确认按钮添加事件,执行退出应用操作
	 * tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
	 * tv_paizhao.setText("拍照"); tv_paizhao.setOnClickListener(this); tv_xiangce
	 * = (TextView) window.findViewById(R.id.tv_content2);
	 * tv_xiangce.setText("相册"); tv_xiangce.setOnClickListener(this); }
	 */
	@SuppressLint("SdCardPath")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*
		 * String filePath = "";
		 * System.out.println("111111111111111111111111111111111111"
		 * +"requestCode="+requestCode);
		 * 
		 * if (data != null) { headImageUri = data.getData();
		 * 
		 * if (resultCode == RESULT_OK) { switch (requestCode) { case
		 * PHOTO_REQUEST_GALLERY:// 直接从相册获取 if (data != null) {
		 * 
		 * if (headImageUri != null) {
		 * filePath=startPhotoZoom(headImageUri,480).toString(); filePath =
		 * PictureUtils.getPickPhotoPath(this, headImageUri); headImageFile =
		 * new File(filePath); iv_photo.setImageURI(headImageUri); } } break;
		 * case PHOTO_REQUEST_TAKEPHOTO:// 调用相机拍照 try {
		 * iv_photo.setImageBitmap(BitmapFactory.decodeStream(new
		 * FileInputStream(headImageFile))); } catch (FileNotFoundException e) {
		 * e.printStackTrace(); } if(headImageFile.exists()){
		 * System.out.println(headImageFile+"---------"); }else{
		 * System.out.println("meiyou!!!"); } break; case PHOTO_REQUEST_CUT:
		 * System.out.println(headImageUri);
		 * 
		 * System.out.println("111111111111111111111111111111111111"+filePath);
		 * iv_photo.setImageBitmap(BitmapFactory.decodeFile(filePath));
		 * //iv_photo.setImageURI(headImageUri); break;
		 * 
		 * } } }
		 */
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
