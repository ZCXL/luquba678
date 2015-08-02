package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.List;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.MainFragmentPagerAdapt;
import cn.luquba678.activity.fragment.TabMyStoryFragment;
import cn.luquba678.activity.welcome.MainActivity;
import cn.luquba678.activity.welcome.SharedConfig;
import cn.luquba678.utils.ImageUtil;
import cn.luquba678.view.MainViewPager;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragmentActivity extends FragmentActivity implements
		OnPageChangeListener {
	public static boolean flagFind = true;
	public MainViewPager viewPager;
	public Fragment fragments[] = null;
	private static ImageView[] tabs = null;
	private static LinearLayout[] lintabs = null;
	private static TextView[] tvtabs = null;

	// private static ArrayList<LinearLayout> lintabs = new
	// ArrayList<LinearLayout>();
	// private static ArrayList<ImageView> tabs = new ArrayList<ImageView>();
	private static int currIndex = 0;// 当前页卡编号
	private FragmentManager fragmentManager;
	private int navigationLength = Resources.classes.length;
	private Intent intentService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_main);
		initViews();// 初始化控件
		try {
			fragments = new Fragment[navigationLength];
			for (int i = 0; i < navigationLength; i++) {
				fragments[i] = (Fragment) Resources.classes[i].newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		fragmentManager = this.getSupportFragmentManager();
		viewPager = (MainViewPager) findViewById(R.id.viewPager);
		// viewPager.setSlipping(false);//设置ViewPager是否可以滑动
		viewPager.setOffscreenPageLimit(5);
		viewPager.setOnPageChangeListener(this);
		// viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setAdapter(new MainFragmentPagerAdapt(fragmentManager,
				fragments));
		viewPager.setCurrentItem(0);// 选择起始页
		// onPageSelected(0);
		// lintabs.get(2).performClick();
	}

	/**
	 * @Title: initViews
	 * @Description: TODO初始化控件
	 */
	public void initViews() {
		tvtabs = new TextView[navigationLength];
		lintabs = new LinearLayout[navigationLength];
		tabs = new ImageView[navigationLength];
		for (int i = 0; i < navigationLength; i++) {
			tvtabs[i] = (TextView) findViewById(Resources.navigationText[i]);
			lintabs[i] = (LinearLayout) findViewById(Resources.navigationLayoutIds[i]);
			tabs[i] = (ImageView) findViewById(Resources.navigationImgViewIds[i]);
			lintabs[i].setOnClickListener(new MyOnClickListener(i));
		}
	}

	/**
	 * @ClassName: MyOnPageChangeListener
	 * @Description: TODO页卡切换监听
	 * @author Collin
	 *
	 */
	@Override
	public void onPageSelected(int i) {
		int[] iconsNormal = Resources.iconsNormal;
		// 前一个图标变回去
		ImageUtil.setDraById(tabs[currIndex], iconsNormal[currIndex], this);
		tvtabs[currIndex].setTextColor(getResources().getColor(
				Resources.textcolors[0]));
		int[] ic = Resources.iconsChoosed;
		// 选择的图标变色
		ImageUtil.setDraById(tabs[i], ic[i], this);
		tvtabs[i]
				.setTextColor(getResources().getColor(Resources.textcolors[1]));
		currIndex = i;
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	/**
	 * @ClassName: MyOnClickListener
	 * @Description: TODO头标点击监听
	 * @author Panyy
	 * @date 2013 2013年11月6日 下午2:46:08
	 *
	 */
	private class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			//viewPager.setCurrentItem(index);
			viewPager.setCurrentItem(index,false);
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}
	@Override
	protected void onStop() {
		/*
		 * if (intentService != null) { unbindService(conn);
		 * this.stopService(intentService); }
		 */
		super.onStop();
	}

	@Override
	protected void onPause() {
		/*
		 * if (intentService != null) { this.stopService(intentService);
		 * unbindService(conn); }
		 */
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// int page = viewPager.getCurrentItem();
		onPageSelected(viewPager.getCurrentItem());
	}
}
