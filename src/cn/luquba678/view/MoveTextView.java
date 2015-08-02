package cn.luquba678.view;

import java.util.Arrays;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.text.style.TypefaceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import cn.luquba678.R;

public class MoveTextView extends View {
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
	private int style = Typeface.BOLD;
	private int size = 82;
	int textColor = Color.BLACK, shadowColor = Color.WHITE;
	private Paint paint1;
	private float TextHeight;

	public MoveTextView(Context context, String text, int textColor,
			int shadowColor) {
		super(context);
		manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowHeight = manager.getDefaultDisplay().getHeight() - 200;
		windowWidth = manager.getDefaultDisplay().getWidth();
		this.text = text;
		this.textColor = textColor;
		this.shadowColor = shadowColor;
	}

	public MoveTextView(Context context, String text) {
		super(context);
		manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowHeight = manager.getDefaultDisplay().getHeight() - 200;
		windowWidth = manager.getDefaultDisplay().getWidth();
		this.text = text;
	}

	public void setTextWidth(float textWidth) {
		TextWidth = textWidth;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if (bitmap == null) {
			setBitmap();
		}
		canvas.drawBitmap(bitmap, 0, 0, null);
		SetMyWish(text, canvas);
	}

	public Bitmap setBitmap() {
		Resources resources = getResources();
		bitmap = Bitmap.createBitmap((int) windowHeight, (int) windowWidth,
				Bitmap.Config.ARGB_8888);
		bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(),
				true);
		BitmapWidth = bitmap.getWidth();

		BitmapHeight = bitmap.getHeight();
		return bitmap;
	}

	public Bitmap setBitmap(Bitmap bitmap) {
		this.bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(),
				getHeight(), true);
		invalidate();
		return bitmap;
	}

	public void setTextColor(int textColor, int shadowColor) {
		this.shadowColor = shadowColor;
		this.textColor = textColor;
		invalidate();

	}

	public void setTextSize(int size) {
		this.size = size;
		invalidate();

	}

	public void setStyle(int style) {
		this.style = style;
		invalidate();
	}

	/**
	 * 设置文字
	 * 
	 * @param Content
	 */
	public void SetMyWish(String Content, Canvas canvas) {
		paint1 = new Paint();
		paint1.setTextSize(size);
		paint1.setColor(shadowColor);
		paint1.setFakeBoldText(true);
		paint1.setAntiAlias(true);
		paint1.setTypeface(type);
		paint1.setStrokeWidth(4f);
		paint1.setStyle(Paint.Style.STROKE);
		paint = new Paint();
		paint.setTextSize(size);
		paint.setColor(textColor);
		paint.setAntiAlias(true);
		paint.setFakeBoldText(true);
		paint.setTypeface(type);
		FontMetrics fm = paint.getFontMetrics();
		float baseline = fm.descent - fm.ascent;
		float y1 = baseline; // 由于系统基于字体的底部来绘制文本，所有需要加上字体的高度。
		// 文本自动换行
		String[] texts = autoSplit(Content, windowWidth - 100, baseline
				+ fm.leading);
		TextWidth = paint.measureText(texts[0]);
		TextHeight = texts.length * (baseline + fm.leading);
		System.out.println(texts[0]);
		int i = 0;
		for (String evreytext : texts) {
			canvas.drawText(evreytext, x, y + y1, paint1); // 坐标以控件左上角为原点
			canvas.drawText(evreytext, x, y + y1, paint); // 坐标以控件左上角为原点
			y1 += baseline + fm.leading; // 添加字体行间距
		}

	}

	public void setType(Typeface type) {
		this.type = type;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = event.getX() - TextWidth / 2;
			System.out.println("TextWidth=" + TextWidth + "----TextHeight="
					+ TextHeight);
			y = event.getY() - TextHeight / 2;
			autoBoder();
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			x = event.getX() - TextWidth / 2;
			System.out.println("TextWidth=" + TextWidth + "----TextHeight="
					+ TextHeight);
			y = event.getY() - TextHeight / 2;
			autoBoder();
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("TextWidth=" + TextWidth + "----TextHeight="
					+ TextHeight);
			x = event.getX() - TextWidth / 2;
			y = event.getY() - TextHeight / 2;

			autoBoder();
			invalidate();
			break;

		default:
			break;
		}
		return true;
	}

	private String[] autoSplit(String content, float width, float f) {
		int length = content.length();
		float textWidth = paint.measureText(content);
		if (textWidth <= width) {
			return new String[] { content };
		}

		int start = 0, end = 1;
		int lines = (int) Math.ceil(textWidth / width); // 计算行数
		String[] lineTexts = new String[lines];
		if (lines > 1) {
			for (int i = 0;;) {
				if (paint.measureText(content, start, end) > width) { // 文本宽度超出控件宽度时
					lineTexts[i++] = (String) content.subSequence(start, end);
					start = end;
				} else if (end == length) { // 不足一行的文本
					lineTexts[i] = (String) content.subSequence(start, end);
					break;
				}
				end += 1;
			}
		} else {
			lineTexts[0] = content;
		}
		return lineTexts;
	}

	private void autoBoder() {
		if (left + BitmapWidth < windowWidth) {

			if (x >= left + BitmapWidth - TextWidth) {
				x = left + BitmapHeight - TextWidth;

			} else if (x < left) {
				x = left;
			}
		} else {
			if (x >= windowWidth - TextWidth) {// 右边界
				x = windowWidth - TextWidth;
			} else if (x <= 0) {// 左边界
				x = 0;
			}
		}
		if (top + BitmapHeight < windowHeight) {
			if (y >= top + BitmapHeight - TextHeight) {
				y = top + BitmapHeight - TextHeight;
			} else if (y <= top) {
				y = top;
			}
		} else {
			if (y >= windowHeight - TextHeight - 120) {
				y = windowHeight - TextHeight - 120;
			} else if (y <= 0) {
				y = 0;
			}
		}
	}

}
