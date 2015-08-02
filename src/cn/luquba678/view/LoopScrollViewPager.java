package cn.luquba678.view;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 图片滚动类
 * 
 * @author Administrator
 * 
 */
public class LoopScrollViewPager extends ViewPager {

	int mScrollTime = 0;
	Timer timer;
	int oldIndex = 0;
	int curIndex = 0;

	public LoopScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 开始广告滚动
	 * 
	 * @param mainActivity
	 *            显示广告的主界面
	 * @param imgList
	 *            图片列表, 不能为null ,最好是三张
	 */
	public void start(Activity mainActivity, List<View> imgList) {
		this.setAdapter(new MyPagerAdapter(imgList));// 设置适配器
		// System.out.println(Integer.MAX_VALUE);
		if (imgList.size() > 1) {
			this.setCurrentItem((Integer.MAX_VALUE / 2)
					- (Integer.MAX_VALUE / 2) % imgList.size());// 设置选中为中间/图片为和第0张一样
		}
	}

	/**
	 * 取得当明选中下标
	 * 
	 * @return
	 */
	public int getCurIndex() {
		return curIndex;
	}

	/**
	 * 停止滚动
	 */
	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	// 适配器 //循环设置
	private class MyPagerAdapter extends PagerAdapter {
		List<View> mListViews; // 图片组

		public MyPagerAdapter(List<View> mListViews) {
			super();
			this.mListViews = mListViews;
		}

		public void finishUpdate(View arg0) {
		}

		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		public int getCount() {
			if (mListViews.size() == 1) {// 一张图片时不用流动
				return mListViews.size();
			}
			return Integer.MAX_VALUE;
		}

		public Object instantiateItem(View v, int i) {
			if (((ViewPager) v).getChildCount() == mListViews.size()) {
				((ViewPager) v)
						.removeView(mListViews.get(i % mListViews.size()));
			}
			((ViewPager) v).addView(mListViews.get(i % mListViews.size()), 0);
			return mListViews.get(i % mListViews.size());
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
		}
	}
}
