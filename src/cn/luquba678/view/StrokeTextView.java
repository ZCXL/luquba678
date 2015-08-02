package cn.luquba678.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import cn.luquba678.R;

public class StrokeTextView extends TextView {

	private TextView borderText = null;// /用于描边的TextView

	public StrokeTextView(Context context) {
		super(context);
		borderText = new TextView(context);
		init(context);
	}

	public StrokeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		borderText = new TextView(context, attrs);
		init(context);
	}

	public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		borderText = new TextView(context, attrs, defStyle);
		init(context);
	}

	public void init(Context context) {
		manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowHeight = manager.getDefaultDisplay().getHeight() - 200;
		windowWidth = manager.getDefaultDisplay().getWidth();
		TextPaint tp = borderText.getPaint();
		tp.setStrokeWidth(4); // 设置描边宽度
		tp.setStyle(Style.STROKE); // 对文字只描边
		borderText.setTextColor(outColor); // 设置描边颜色
		borderText.setGravity(getGravity());
	}

	int outColor = getResources().getColor(R.color.white);

	public void setColor(int color, int outColor) {
		this.outColor = outColor;
		this.setTextColor(color);
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params) {
		super.setLayoutParams(params);
		borderText.setLayoutParams(params);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		CharSequence tt = borderText.getText();

		// 两个TextView上的文字必须一致
		if (tt == null || !tt.equals(this.getText())) {
			borderText.setText(getText());
			this.postInvalidate();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		borderText.measure(widthMeasureSpec, heightMeasureSpec);
	}

	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		borderText.layout(left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		borderText.draw(canvas);
		super.onDraw(canvas);
	}

	@Override
	public void setTextSize(float size) {
		// TODO Auto-generated method stub
		super.setTextSize(size);
		borderText.setTextSize(size);
	}

	@Override
	public void setTextSize(int unit, float size) {
		// TODO Auto-generated method stub
		super.setTextSize(unit, size);
		borderText.setTextSize(unit, size);
	}

	private void autoBoder() {
		if (left + BitmapWidth < windowWidth) {

			if (x >= left + BitmapWidth - TextWidth) {
				x = left + BitmapHeight - TextWidth;

			} else if (x < left) {
				x = left;
			}
		} else {
			// 右边界
			if (x >= windowWidth - TextWidth) {
				x = windowWidth - TextWidth;
			}
			// 左边界
			else if (x <= 0) {
				x = 0;
			}
		}
		if (top + BitmapHeight < windowHeight) {
			// 下
			if (y >= top + BitmapHeight) {
				y = top + BitmapHeight;
			}
			// 上
			else if (y <= top + 10) {
				y = top + 10;
			}
		} else {
			if (y >= windowHeight) {
				y = windowHeight;
			} else if (y <= 0) {
				y = 0;
			}
		}
		System.out.println("x:        " + x + "y:      " + y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("ACTIONACTIONACTIONACTIONACTION");
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("ACTION_DOWN");
			x = event.getX() - TextWidth / 2;
			y = event.getY();
			autoBoder();
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			x = event.getX() - TextWidth / 2;
			y = event.getY();
			System.out.println("ACTION_MOVE");
			autoBoder();
			invalidate();

			break;
		case MotionEvent.ACTION_UP:
			System.out.println("ACTION_UP");
			x = event.getX() - TextWidth / 2;
			y = event.getY();

			autoBoder();
			invalidate();
			break;

		default:
			break;
		}
		return true;
	}

	private float x = 200;
	private float y = 200;
	Paint paint;
	WindowManager manager;
	private static Canvas canvas1;
	float TextWidth = 0;
	private static float windowWidth;
	private static float windowHeight;
	private static float BitmapWidth;
	private static float BitmapHeight;
	private static float left = 0; // 图片在屏幕中位置X坐标
	private static float top = 0; // 图片在屏幕中位置Y坐标
	private String text;
	private Bitmap bitmap;
	private Typeface type = Typeface.DEFAULT;
	private int color = Color.BLACK;
	private int style = Typeface.BOLD;
	private int size = 82;
}