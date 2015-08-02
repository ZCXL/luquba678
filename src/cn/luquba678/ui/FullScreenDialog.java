package cn.luquba678.ui;

import cn.luquba678.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class FullScreenDialog extends Dialog {

	/**
	 * 
	 */
	public Activity activity;

	public FullScreenDialog(Context context) {
		super(context, R.style.Dialog_Fullscreen);
		activity = (Activity) context;
	}

	public <T extends View> T getView(int viewId) {
		return (T) findViewById(viewId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFullScreen();
	}

	/**
	 * 
	 */
	private void setFullScreen() {
		LayoutParams lay = getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Rect rect = new Rect();
		View view = getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.height = dm.heightPixels - rect.top;
		lay.width = dm.widthPixels;
	}

	/**
	 * @param view
	 *            View
	 * @param title
	 *            View
	 */
	public void initTitle(View view, String title) {
		view.findViewById(R.id.title_top_image).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});

		TextView textView = (TextView) view.findViewById(R.id.top_text);
		textView.setText(title);
	}

}
