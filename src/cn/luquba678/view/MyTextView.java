package cn.luquba678.view;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class MyTextView extends TextView {

	private float x, y;
	private int state;
	private float StartX;
	private float StartY;
	private float mTouchStartX;
	private float mTouchStartY;

	public MyTextView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY() - 25; // 25是系统状态栏的高度
		Log.i("currP", "currX" + x + "====currY" + y);// 调试信息
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			state = MotionEvent.ACTION_DOWN;
			StartX = x;
			StartY = y;
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			Log.i("startP", "startX" + mTouchStartX + "====startY"
					+ mTouchStartY);// 调试信息
			break;
		case MotionEvent.ACTION_MOVE:
			state = MotionEvent.ACTION_MOVE;
			// updateViewPosition();
			break;

		case MotionEvent.ACTION_UP:
			state = MotionEvent.ACTION_UP;

			// updateViewPosition();
			// showImg();
			mTouchStartX = mTouchStartY = 0;
			break;
		}
		return true;

	}

}
