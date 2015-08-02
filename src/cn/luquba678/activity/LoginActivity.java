package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.luquba678.R;
import cn.luquba678.activity.listener.TextLenthWatcher;
import cn.luquba678.activity.welcome.SharedConfig;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.utils.CacheUtil;
import cn.luquba678.utils.MD5;
import cn.luquba678.utils.SPUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends CommonActivity implements OnClickListener,
		TextWatcher, OnFocusChangeListener {

	private ListView listview_stories;
	private EditText username;
	private ProgressDialog dialog;
	private EditText password;
	private ScrollView mScrollView;
	private Button do_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setOnClickLinstener(R.id.go_regist, R.id.go_reset_password,
				R.id.do_login_btn, R.id.login_wechat, R.id.login_weibo,
				R.id.login_qq);
		ShareSDK.initSDK(this);
		dialog = new ProgressDialog(LoginActivity.this);
		do_login = getView(R.id.do_login_btn);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		// username.addTextChangedListener(this);
		// password.addTextChangedListener(this);
		username.setOnFocusChangeListener(this);
		password.setOnFocusChangeListener(this);
		mScrollView = (ScrollView) findViewById(R.id.loging_scroll);
		password.addTextChangedListener(new TextLenthWatcher(do_login, 6));
		remberMe();
	}

	private SharedPreferences sharedPreferences;
	private Editor editor;

	private void saveUserInfo(String str) {
		editor.putString(User.LUQUBA_USER_INFO, str);
		editor.commit();
	}

	private void remberMe() {
		editor = User.getUserEditor(self);
		sharedPreferences = getSharedPreferences("luquba_login",
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();// 获取编辑器

		String userName = sharedPreferences.getString(User.TEL, "");
		String pwd = sharedPreferences.getString(User.PASSWORD, "");
		username.setText(userName);
		password.setText(pwd);
	}

	public void loginService(final String tel, final String pass) {
		editor.putString(User.TEL, tel);
		editor.putString(User.PASSWORD, pass);
		editor.commit();// 提交修改
		// final String MD5Pwd = pass;
		MultipartEntity entity = new MultipartEntity();

		dialog.setMessage("正在登录...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		try {
			entity.addPart("tel", new StringBody(tel));
			entity.addPart("password", new StringBody(pass));

			LoadDataFromServer task = new LoadDataFromServer(Const.LOGIN_URL,
					entity);
			task.getData(new DataCallBack() {

				@Override
				public void onDataCallBack(int what, Object data) {

					if (what == 200 && data != null) {
						try {
							JSONObject jsonObject = JSONObject.parseObject(data
									.toString());
							Log.i("wyb", "jsonObject is " + jsonObject.toString());
							int code = jsonObject.getInteger("errcode");
							if (code == 0) {
								String jsonObj = jsonObject.get("user").toString();
								JSONObject jsonUser = JSONObject.parseObject(jsonObj);
								SPUtils.put(LoginActivity.this, "address",jsonUser.get("address").toString());
								SPUtils.put(LoginActivity.this, "birth",jsonUser.get("birth").toString());
								SPUtils.put(LoginActivity.this, "grade",jsonUser.get("grade").toString());
								SPUtils.put(LoginActivity.this, "headpic",jsonUser.get("headpic").toString());
								SPUtils.put(LoginActivity.this, "intro",jsonUser.get("intro").toString());
								SPUtils.put(LoginActivity.this, "nickname",jsonUser.get("nickname").toString());
								SPUtils.put(LoginActivity.this, "sex",jsonUser.get("sex").toString());
								SPUtils.put(LoginActivity.this, "type",jsonUser.get("type").toString());
								SPUtils.put(LoginActivity.this, "year",jsonUser.get("year").toString());
								dialog.dismiss();
								String login_token = jsonObject
										.getString("login_token");
								saveUserInfo(data.toString());
								Gson gson = new Gson();
								User u = gson.fromJson(jsonObject.get("user")
										.toString(), User.class);

								editor.putString("login_token", login_token);
								editor.putString("uid", u.getUid() + "");
								editor.commit();
								Intent intent = new Intent(self,
										MainFragmentActivity.class);
								self.startActivity(intent);
								self.finish();
								// toast(login_token+jsonObject.get("user").toString()+u.getTel());
							} else if (code == 1) {
								dialog.dismiss();
								Toast.makeText(LoginActivity.this, "密码错误...",
										Toast.LENGTH_SHORT).show();
							} else if (code == 3) {
								dialog.dismiss();
								Toast.makeText(LoginActivity.this,
										"服务器端注册失败...", Toast.LENGTH_SHORT)
										.show();
							} else {
								dialog.dismiss();
								Toast.makeText(LoginActivity.this,
										"服务器繁忙请重试...", Toast.LENGTH_SHORT)
										.show();
							}

						} catch (JSONException e) {
							dialog.dismiss();
							Toast.makeText(LoginActivity.this, "数据解析错误...",
									Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}

					} else {
						dialog.dismiss();
						Toast.makeText(LoginActivity.this, "服务器出错...",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void toast(String str) {
		Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.do_login_btn:
			String user = username.getText().toString();
			String pass = password.getText().toString();
			loginService(user, pass);
			break;
		case R.id.go_regist:
			Intent registIntent = new Intent(this, RegisterActivity.class);
			this.startActivity(registIntent);
			break;
		case R.id.go_reset_password:
			Intent resetpassIntent = new Intent(this, ResetPassActivity1.class);
			this.startActivity(resetpassIntent);
			break;
		case R.id.login_wechat:
			Platform weChat = ShareSDK.getPlatform(this, Wechat.NAME);
			weChat.setPlatformActionListener(mActionListener);
			weChat.authorize();
			break;
		case R.id.login_weibo:
			Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
			weibo.setPlatformActionListener(mActionListener);
			weibo.authorize();
			break;
		case R.id.login_qq:
			Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
			qq.setPlatformActionListener(mActionListener);
			qq.authorize();
			break;
		default:
			break;
		}
	}

	PlatformActionListener mActionListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> res) {
			Message msg = new Message();
			msg.obj = arg0;
			msg.what = 1;
			mhandler.sendMessage(msg);
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
		}
	};
	private ImageView login_wechat, login_weibo, login_qq;
	Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Platform arg0 = (Platform) msg.obj;
				break;

			default:
				break;
			}

		}

	};

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus)
			mScrollView.scrollTo(0, 200);
	}

	public void loginOut() {
		sharedPreferences = getSharedPreferences("luquba_login",
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();// 获取编辑器
		editor.putString("login_token", "");
		editor.putString("uid", "");
		editor.commit();
	}
}
