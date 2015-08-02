package cn.luquba678.activity.welcome;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;

import cn.luquba678.R;
import cn.luquba678.activity.CommonActivity;
import cn.luquba678.activity.LoginActivity;
import cn.luquba678.activity.MainFragmentActivity;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.CircularImage;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

//第一次运行的引导页代码
public class WelcomeActivity extends CommonActivity implements
		OnPageChangeListener, OnClickListener {
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private Button startButton, skip_btn;
	private LinearLayout indicatorLayout;
	private ArrayList<View> views;
	private ImageView[] indicators = null;
	private int[] images;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		// 创建桌面快捷方式
		new CreateShut(this);
		// 设置引导图片
		// 仅需在这设置图片 指示器和page自动添加
		images = new int[] { R.drawable.welcome1, R.drawable.welcome2,
				R.drawable.welcome3 };
		initView();
	}

	// 初始化视图
	private void initView() {
		// 实例化视图控件
		viewPager = (ViewPager) findViewById(R.id.viewpage);
		startButton = getView(R.id.start_Button);
		skip_btn = getView(R.id.skip);
		setOnClickLinstener(R.id.start_Button, R.id.skip);
		indicatorLayout = (LinearLayout) findViewById(R.id.indicator);
		views = new ArrayList<View>();
		indicators = new ImageView[images.length]; // 定义指示器数组大小
		for (int i = 0; i < images.length; i++) {
			// 循环加入图片
			View imageView = new View(this);
			imageView.setBackgroundResource(images[i]);
			views.add(imageView);
			// 循环加入指示器
			indicators[i] = new ImageView(this);
			// indicators[i].setLayoutParams(new LayoutParams(100, 100));
			indicators[i]
					.setBackgroundResource(R.drawable.indicators_default_xml);
			if (i == 0) {
				indicators[i]
						.setBackgroundResource(R.drawable.indicators_now_xml);
			}
			indicatorLayout.addView(indicators[i]);
		}
		pagerAdapter = new BasePagerAdapter(views);
		viewPager.setAdapter(pagerAdapter); // 设置适配器
		viewPager.setOnPageChangeListener(this);
		SharedPreferences shared = new SharedConfig(this).GetConfig();
		Editor editor = shared.edit();
		editor.putBoolean("First", false);
		editor.commit();
	}

	// 按钮的点击事件
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, LoginActivity.class);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	// 监听viewpage
	@Override
	public void onPageSelected(int arg0) {
		// 显示最后一个图片时显示按钮
		if (arg0 == indicators.length - 1) {
			startButton.setVisibility(View.VISIBLE);
			skip_btn.setVisibility(View.INVISIBLE);
		} else {
			skip_btn.setVisibility(View.VISIBLE);
			startButton.setVisibility(View.INVISIBLE);
		}
		// 更改指示器图片
		for (int i = 0; i < indicators.length; i++) {
			indicators[arg0]
					.setBackgroundResource(R.drawable.indicators_now_xml);
			if (arg0 != i) {
				indicators[i]
						.setBackgroundResource(R.drawable.indicators_default_xml);
			}
		}
	}
}
