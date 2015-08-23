package cn.luquba678.activity.welcome;

import com.alibaba.fastjson.JSONObject;
import com.zhuchao.http.Network;
import com.zhuchao.receiver.NetworkReceiver;

import cn.luquba678.R;
import cn.luquba678.activity.LoginActivity;
import cn.luquba678.activity.MainFragmentActivity;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.Until;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

//软件入口，闪屏界面。
public class MainActivity extends Activity {
	private boolean first; // 判断是否第一次打开软件
	private View view;
	private Animation animation;
	private SharedPreferences shared;
	private static int TIME = 1000;
	public static boolean isSingle;
    private NetworkConnectReceiver networkConnectReceiver;

	// 进入主程序的延迟时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.activity_main_start_page, null);
		isSingle = true;
		setContentView(view);
		shared = new SharedConfig(this).GetConfig(); // 得到配置文件
        /**
         * while network is connected,update view.
         */
        IntentFilter filter=new IntentFilter();
        filter.addAction(NetworkReceiver.NETWORK_CONNECT);
        filter.addAction(NetworkReceiver.NETWORK_DISCONNECT);
        networkConnectReceiver=new NetworkConnectReceiver();
        this.registerReceiver(networkConnectReceiver, filter);

        if(Network.checkNetWorkState(MainActivity.this)){
            into();
        }else {
            /**
             * 如果网络不可用，则弹出对话框，对网络进行设置
             */
            Until.showConnectNetDialog(MainActivity.this);
        }

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * automatic login
     * @return
     */
	private boolean checkLogin() {
		// TODO Auto-generated method stub

		String url = String.format(Const.CHECK_LOGIN_STATE, User.getUID(this), User.getLoginToken(this));

		try {
            String json = HttpUtil.postRequestEntity(url, null);
			JSONObject obj = JSONObject.parseObject(json);
			Integer err_code = obj.getInteger("errcode");
			if (err_code == 0) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

    @Override
    public void onDestroy(){
        if(networkConnectReceiver!=null){
            unregisterReceiver(networkConnectReceiver);
        }
        super.onDestroy();
    }
    /**
     *  进入主程序的方法
      */
	public void ifWelcome() {
		// goHomePage();
		Intent intent;
		// intent = new Intent(this, WelcomeActivity.class);

		if (first) {
			intent = new Intent(this, WelcomeActivity.class);
		} else {
			if (checkLogin()) {

				intent = new Intent(this, MainFragmentActivity.class);
			} else {
				intent = new Intent(this, LoginActivity.class);

			}
		}

		startActivity(intent);
		// 设置Activity的切换效果
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		this.finish();
	}

    /**
     * logic
     */
	private void into() {
        // 如果网络可用则判断是否第一次进入，如果是第一次则进入欢迎界面
        first = shared.getBoolean("First", true);
        // 设置动画效果是alpha，在anim目录下的alpha.xml文件中定义动画效果
        animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        // 给view设置动画效果
        view.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            // 这里监听动画结束的动作，在动画结束的时候开启一个线程，这个线程中绑定一个Handler,并
            // 在这个Handler中调用goHome方法，而通过postDelayed方法使这个方法延迟500毫秒执行，达到
            // 达到持续显示第一屏500毫秒的效果
            @Override
            public void onAnimationEnd(Animation arg0) {
                new Handler().postDelayed(new Runnable() {

                    // @SuppressLint("NewApi")
                    @Override
                    public void run() {
                        ifWelcome();
                    }

                }, TIME);
            }
        });
	}

    /**
     * receive broadcast to update
     */
    public class NetworkConnectReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals( NetworkReceiver.NETWORK_CONNECT)){
                into();
            }else if(intent.getAction().equals(NetworkReceiver.NETWORK_DISCONNECT)){
                Until.showConnectNetDialog(MainActivity.this);
            }
        }
    }
}
