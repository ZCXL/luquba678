package cn.luquba678.activity;

import cn.luquba678.utils.Until;
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
import com.zhuchao.receiver.NetworkReceiver;
import com.zhuchao.utils.FileSystem;
import com.zhuchao.view_rewrite.LoadingDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;

import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import cn.luquba678.R;
import cn.luquba678.activity.listener.TextLenthWatcher;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.utils.SPUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends CommonActivity implements OnClickListener, TextWatcher, OnFocusChangeListener {

	private EditText username;
	private EditText password;
	private ScrollView mScrollView;
	private Button do_login;

    private NetworkConnectReceiver networkConnectReceiver;
    private LoadingDialog loadingDialog;
    private Info info;


    private SharedPreferences sharedPreferences;
    private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setOnClickLinstener(R.id.go_regist, R.id.go_reset_password, R.id.do_login_btn, R.id.login_wechat, R.id.login_weibo, R.id.login_qq);
		ShareSDK.initSDK(this);
		do_login = getView(R.id.do_login_btn);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		username.setOnFocusChangeListener(this);
		password.setOnFocusChangeListener(this);
		mScrollView = (ScrollView) findViewById(R.id.loging_scroll);
		password.addTextChangedListener(new TextLenthWatcher(do_login, 6));

        loadingDialog=new LoadingDialog(LoginActivity.this);
        /**
         * while network is connected,update view.
         */
        IntentFilter filter=new IntentFilter();
        filter.addAction(NetworkReceiver.NETWORK_CONNECT);
        filter.addAction(NetworkReceiver.NETWORK_DISCONNECT);
        networkConnectReceiver=new NetworkConnectReceiver();
        this.registerReceiver(networkConnectReceiver, filter);

        /**
         * load info that is saved in Share Memory
         */
		rememberMe();
	}

    @Override
    public void onDestroy(){
        if(networkConnectReceiver!=null){
            unregisterReceiver(networkConnectReceiver);
        }
        super.onDestroy();
    }


	private void saveUserInfo(String str) {
		editor.putString(User.LUQUBA_USER_INFO, str);
		editor.commit();
	}

	/**
	 * get user info who login last time
	 * and set value of edit text
	 */
	private void rememberMe() {
		editor = User.getUserEditor(self);
		sharedPreferences = getSharedPreferences("luquba_login", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();// 获取编辑器

		String userName = sharedPreferences.getString(User.TEL, "");
		String pwd = sharedPreferences.getString(User.PASSWORD, "");
		username.setText(userName);
		password.setText(pwd);
	}

	/**
	 * login by using telephone number
	 * @param tel
	 * @param pass
	 */
	public void loginService(final String tel, final String pass) {
		//set new value of user
		editor.putString(User.TEL, tel);
		editor.putString(User.PASSWORD, pass);
		editor.commit();// 提交修改

		MultipartEntity entity = new MultipartEntity();

        loadingDialog.startProgressDialog();

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
								SPUtils.put(LoginActivity.this,"login_type","phone_number");

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


							} else if (code == 50003) {
								toast("密码错误...");

							} else if (code == 3) {
								toast("服务器端注册失败...");

							} else {
								toast("服务器繁忙请重试...");

							}

						} catch (JSONException e) {
							toast("数据解析错误...");
							e.printStackTrace();
						}finally {
                            loadingDialog.stopProgressDialog();
                        }

					} else {
                        loadingDialog.stopProgressDialog();
						toast("服务器出错...");
					}
				}
			});
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
            loadingDialog.stopProgressDialog();
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
			Intent registerIntent = new Intent(this, RegisterActivity.class);
			this.startActivity(registerIntent);
			break;
		case R.id.go_reset_password:
			Intent resetPassIntent = new Intent(this, ResetPassActivity1.class);
			this.startActivity(resetPassIntent);
			break;
		case R.id.login_wechat:
			Platform weChat = ShareSDK.getPlatform(this, Wechat.NAME);
			weChat.removeAccount();
			weChat.setPlatformActionListener(mActionListener);
			weChat.showUser(null);
            loadingDialog.startProgressDialog();
			break;
		case R.id.login_weibo:
			Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
			weibo.removeAccount();
			weibo.setPlatformActionListener(mActionListener);
			weibo.SSOSetting(true);
			weibo.showUser(null);
            loadingDialog.startProgressDialog();
			break;
		case R.id.login_qq:
			Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
			qq.removeAccount();
			qq.setPlatformActionListener(mActionListener);
			qq.showUser(null);
            loadingDialog.startProgressDialog();
			break;
		default:
			break;
		}
	}

	PlatformActionListener mActionListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
            mHandler.sendEmptyMessage(1);
		}

		@Override
		public void onComplete(Platform arg0, int arg1, HashMap<String, Object> res) {


            String platformName=arg0.getName();
            info=new Info();
            if(platformName.equals(SinaWeibo.NAME)){
                //get weibo info
                StringBuilder builder=new StringBuilder();
                Iterator iter = res.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    builder.append(String.valueOf(key) + ":" + String.valueOf(val) + "\n");
                }
                FileSystem.saveFile(builder.toString(),"weibo");
                info.nickname=res.get("name").toString();
                info.head_pic=res.get("avatar_hd").toString();
                info.uid=res.get("id").toString();
				info.type="weibo";
            }else if(platformName.equals(Wechat.NAME)){
                StringBuilder builder=new StringBuilder();
				Iterator iter = res.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					Object val = entry.getValue();
					builder.append(String.valueOf(key) + ":" + String.valueOf(val) + "\n");
				}
                FileSystem.saveFile(builder.toString(),"wechat");
                info.nickname=res.get("nickname").toString();
                info.uid=res.get("unionid").toString();
                info.head_pic=res.get("headimgurl").toString();
                info.type="wechat";
			}else if(platformName.equals(QQ.NAME)){
                StringBuilder builder=new StringBuilder();
                Iterator iter = res.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    builder.append(String.valueOf(key) + ":" + String.valueOf(val) + "\n");
                }
                FileSystem.saveFile(builder.toString(),"qq");
                info.nickname=res.get("nickname").toString();
                info.head_pic=res.get("figureurl_qq_2").toString();
                String temp=new String(info.head_pic);
                temp=temp.substring(temp.indexOf("1104786104/")+11);
                temp=temp.substring(0,temp.indexOf("/"));
                info.uid=temp;
				info.type="qq";
            }

            //next step:login our server
			Message msg = new Message();
			msg.obj = info;
			msg.what = 1;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
            mHandler.sendEmptyMessage(2);
		}
	};
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					otherLogin((Info)msg.obj);
					break;
				case 2:
                    loadingDialog.stopProgressDialog();
					toast("Login failed");
					break;
				case 3:
                    loadingDialog.stopProgressDialog();
					toast("Have canceled login");
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


    /**
     * upload user info from other platform
     * @param info
     */
    private void otherLogin(final Info info){

        MultipartEntity entity = new MultipartEntity();
        try {
            //set key-value about user
            entity.addPart(User.UID, new StringBody(info.uid, Charset.forName("utf-8")));
            entity.addPart(User.NICKNAME, new StringBody(info.nickname, Charset.forName("utf-8")));
            entity.addPart(User.HEADPIC,new StringBody(info.head_pic, Charset.forName("utf-8")));

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
                                SPUtils.put(LoginActivity.this,"login_type",info.type);
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
                                toast("密码错误...");

                            } else if (code == 3) {
                                toast("服务器端注册失败...");

                            } else {
                                toast("服务器繁忙请重试...");

                            }

                        } catch (JSONException e) {
                            toast("数据解析错误...");
                            e.printStackTrace();
                        }finally {
                            loadingDialog.stopProgressDialog();
                        }

                    } else {
                        loadingDialog.stopProgressDialog();
                        toast("服务器出错...");
                    }
                }
            });
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            loadingDialog.stopProgressDialog();
            e1.printStackTrace();
        }
    }

	/**
	 * other platform information
	 */
    class Info{
        String uid;
        String head_pic;
        String nickname;
		String type;
    }
    /**
     * receive broadcast to update
     */
    public class NetworkConnectReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals( NetworkReceiver.NETWORK_CONNECT)){

            }else if(intent.getAction().equals(NetworkReceiver.NETWORK_DISCONNECT)){
                Until.showConnectNetDialog(LoginActivity.this);
            }
        }
    }
}
