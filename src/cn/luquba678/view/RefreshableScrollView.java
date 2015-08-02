package cn.luquba678.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class RefreshableScrollView extends ScrollView {

	public Refresh refresh;
	private float yDown;
	private int touchSlop;
	private SharedPreferences preferences;

	public RefreshableScrollView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public RefreshableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public RefreshableScrollView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			yDown = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float yMove = event.getRawY();
			int distance = (int) (yDown - yMove);
			// 如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
			if (distance <= 0) {
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
		default:
			break;

		}

		return super.onTouchEvent(event);
	}

	public interface Refresh {
		void refresh();
	}

}
