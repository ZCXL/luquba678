package cn.luquba678.activity.welcome;

import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;

import cn.luquba678.R;
import cn.luquba678.activity.CommonActivity;
import cn.luquba678.activity.LoginActivity;
import cn.luquba678.activity.MainFragmentActivity;
import cn.luquba678.activity.person.PersonMessageDialog;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.CacheUtil;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;

//软件入口，闪屏界面。
public class MainActivity extends Activity {
	private boolean first; // 判断是否第一次打开软件
	private View view;
	private Animation animation;
	private NetManager netManager;
	private SharedPreferences shared;
	private static int TIME = 1000;
	public static boolean isSingle;

	// 进入主程序的延迟时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.activity_main_start_page, null);
		isSingle = true;
		setContentView(view);
		shared = new SharedConfig(this).GetConfig(); // 得到配置文件
		netManager = new NetManager(this); // 得到网络管理器
	}

	@Override
	protected void onResume() {
		into();
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	private boolean checkLogin() {
		// TODO Auto-generated method stub

		String url = String.format(Const.QUERY_WORD, User.getUID(this),
				User.getLoginToken(this));

		try {
			String json = HttpUtil.postRequestEntity(url, null);
			JSONObject obj = JSONObject.parseObject(json);
			Integer errcode = obj.getInteger("errcode");
			if (errcode == 0) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	// 进入主程序的方法
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

	private void into() {
		// if (netManager.isOpenNetwork()) {
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
						// goHomePage();
						// Intent intent;
						// 如果第一次，则进入引导页WelcomeActivity
						ifWelcome();
					}

				}, TIME);
			}
		});
		// } else {
		// 如果网络不可用，则弹出对话框，对网络进行设置
		/*
		 * Builder builder = new Builder(context); builder.setTitle("没有可用的网络");
		 * builder.setMessage("是否对网络进行设置?"); builder.setPositiveButton("确定", new
		 * android.content.DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * Intent intent = null; try { String sdkVersion =
		 * android.os.Build.VERSION.SDK; if (Integer.valueOf(sdkVersion) > 10) {
		 * intent = new Intent(
		 * android.provider.Settings.ACTION_WIRELESS_SETTINGS); } else { intent
		 * = new Intent(); ComponentName comp = new ComponentName(
		 * "com.android.settings", "com.android.settings.WirelessSettings");
		 * intent.setComponent(comp);
		 * intent.setAction("android.intent.action.VIEW"); }
		 * MainActivity.this.startActivity(intent); } catch (Exception e) {
		 * e.printStackTrace(); } } }); builder.setNegativeButton("取消", new
		 * android.content.DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * MainActivity.this.finish(); } }); builder.show();
		 */
		// Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
		// }
	}

}
