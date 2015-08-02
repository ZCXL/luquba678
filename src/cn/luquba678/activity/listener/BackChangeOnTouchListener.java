package cn.luquba678.activity.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class BackChangeOnTouchListener implements OnTouchListener {

	private int defaultRes;
	private int pressDownRes;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			v.setBackgroundResource(defaultRes);
			break;
		case MotionEvent.ACTION_DOWN:
			v.setBackgroundResource(pressDownRes);
			break;
		}
		return false;
	}

	public BackChangeOnTouchListener(int defaultRes, int pressDownRes) {
		this.defaultRes = defaultRes;
		this.pressDownRes = pressDownRes;
	}

}
