package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

	private EditText username;
	private ProgressDialog dialog;
	private EditText password;
	private ScrollView mScrollView;
	private Button do_login;

    private Info info;

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

	/**
	 * get user info who logined last time
	 * and set value of edit text
	 */
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

	/**
	 * loging by using telephone number
	 * @param tel
	 * @param pass
	 */
	public void loginService(final String tel, final String pass) {
		//set new value of user
		editor.putString(User.TEL, tel);
		editor.putString(User.PASSWORD, pass);
		editor.commit();// 提交修改

		MultipartEntity entity = new MultipartEntity();

		dialog.setMessage("正在登录...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		try {
			//set key-value about user
			entity.addPart("tel", new StringBody(tel));
			entity.addPart("password", new StringBody(pass));

			LoadDataFromServer task = new LoadDataFromServer(Const.LOGIN_URL,entity);

			//data roll back
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


								//save new value from server
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
								String login_token = jsonObject.getString("login_token");
								saveUserInfo(data.toString());

								//get user info
								Gson gson = new Gson();
								User u = gson.fromJson(jsonObject.get("user").toString(), User.class);

								//set login token and uid
								editor.putString("login_token", login_token);
								editor.putString("uid", u.getUid() + "");
								editor.commit();


								//finish login
								Intent intent = new Intent(self, MainFragmentActivity.class);
								self.startActivity(intent);
								self.finish();


							} else if (code == 1) {

								dialog.dismiss();
								toast("密码错误...");

							} else if (code == 3) {

								dialog.dismiss();
								toast("服务器端注册失败...");

							} else {

								dialog.dismiss();
								toast("服务器繁忙请重试...");

							}

						} catch (JSONException e) {

							dialog.dismiss();
							toast("数据解析错误...");
							e.printStackTrace();
						}

					} else {
						dialog.dismiss();
						toast("服务器出错...");
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
            weChat.SSOSetting(true);
			weChat.showUser(null);
			break;
		case R.id.login_weibo:
			Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
			weibo.setPlatformActionListener(mActionListener);
			weibo.SSOSetting(true);
			weibo.showUser(null);
			break;
		case R.id.login_qq:
			Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
			qq.setPlatformActionListener(mActionListener);
			qq.showUser(null);
			break;
		default:
			break;
		}
	}

	PlatformActionListener mActionListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
            toast("Login failed");
		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> res) {


            String platformName=arg0.getName();
            info=new Info();
            if(platformName.equals(SinaWeibo.NAME)){

                //get weibo info
                info.nickname=res.get("name").toString();
                info.headpic=res.get("avatar_hd").toString();
                info.uid=res.get("id").toString();
            }else if(platformName.equals(Wechat.NAME)){
                Log.d("wyb","lalalalalal");
            }else if(platformName.equals(QQ.NAME)){

                //get qq info
                info.nickname=res.get("nickname").toString();
                info.headpic=res.get("figureurl_qq_2").toString();
                String temp=new String(info.headpic);
                temp=temp.substring(temp.indexOf("1104470925/")+11);
                temp=temp.substring(0,temp.indexOf("/"));
                info.uid=temp;
            }

            //next step:login our server
			Message msg = new Message();
			msg.obj = info;
			msg.what = 1;
			mhandler.sendMessage(msg);
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
            toast("Have canceled login");
		}
	};
	Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				otherLogin((Info)msg.obj);
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

    /**
     * upload user info from other platform
     * @param info
     */
    private void otherLogin(Info info){

        MultipartEntity entity = new MultipartEntity();

        dialog.setMessage("正在登录...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        try {
            //set key-value about user
            entity.addPart(User.UID, new StringBody(info.uid, Charset.forName("utf-8")));
            entity.addPart(User.NICKNAME, new StringBody(info.nickname, Charset.forName("utf-8")));
            entity.addPart(User.HEADPIC,new StringBody(info.headpic, Charset.forName("utf-8")));

            LoadDataFromServer task = new LoadDataFromServer(Const.OTHER_LOGIN_URL,entity);

            //data roll back
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


                                //save new value from server
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
                                String login_token = jsonObject.getString("login_token");
                                saveUserInfo(data.toString());

                                //get user info
                                Gson gson = new Gson();
                                User u = gson.fromJson(jsonObject.get("user").toString(), User.class);

                                //set login token and uid
                                editor.putString("login_token", login_token);
                                editor.putString("uid", u.getUid() + "");
                                editor.commit();


                                //finish login
                                Intent intent = new Intent(self, MainFragmentActivity.class);
                                self.startActivity(intent);
                                self.finish();


                            } else if (code == 1) {

                                dialog.dismiss();
                                toast("密码错误...");

                            } else if (code == 3) {

                                dialog.dismiss();
                                toast("服务器端注册失败...");

                            } else {

                                dialog.dismiss();
                                toast("服务器繁忙请重试...");

                            }

                        } catch (JSONException e) {

                            dialog.dismiss();
                            toast("数据解析错误...");
                            e.printStackTrace();
                        }

                    } else {
                        dialog.dismiss();
                        toast("服务器出错...");
                    }
                }
            });
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    //other platform information
    class Info{
        String uid;
        String headpic;
        String nickname;
    }
}
