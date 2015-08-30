package cn.luquba678.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhuchao.utils.LayoutUtils;

public class MoveTextView extends View {
	/**
	 * default coordinate of text
	 */
	private float x = 200;
	private float y = 200;

	private WindowManager manager;
    private Context context;


	private static float windowWidth;
	private static float windowHeight;
	private static float BitmapWidth;
	private static float BitmapHeight;
	private static float left; // 图片在屏幕中位置X坐标
	private static float top; // 图片在屏幕中位置Y坐标
	private String text;
	/**
	 * background
	 */
	private Bitmap bitmap;
	/**
	 * paint of drawing text
	 */
	private Paint paint;
	private Paint paint1;


	private float TextHeight;
	private float TextWidth = 0;

	//text attributes
	/**
	 * text font
	 */
	private Typeface type = Typeface.DEFAULT;
	/**
	 * text style
	 */
	private int style = Typeface.BOLD;
	/**
	 * text size
	 */
	private int size = 80;
    private int lastSize=size;
	/**
	 * text color
	 */
	private int textColor = Color.BLACK, shadowColor = Color.WHITE;
	/**
	 * text orientation
	 */
	private int orientation= LinearLayout.HORIZONTAL;


    private double nLenStart=0;

	public MoveTextView(Context context, String text, int textColor, int shadowColor) {
		super(context);
		manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowHeight = manager.getDefaultDisplay().getHeight() - LayoutUtils.dpToPx(21.33f, getResources());
		windowWidth = manager.getDefaultDisplay().getWidth()-LayoutUtils.dpToPx(36.66f, getResources());
		this.text = text;
		this.textColor = textColor;
		this.shadowColor = shadowColor;
        this.context=context;
        left = LayoutUtils.dpToPx(36.66f,getResources());
        top=LayoutUtils.dpToPx(8.33f,getResources());

		autoBorder();
	}

	public MoveTextView(Context context, String text) {
		super(context);
		manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowHeight = manager.getDefaultDisplay().getHeight() - LayoutUtils.dpToPx(21.33f, getResources());
        windowWidth = manager.getDefaultDisplay().getWidth()-LayoutUtils.dpToPx(36.66f, getResources());
        this.text = text;
        this.context=context;
        left = LayoutUtils.dpToPx(36.66f, getResources());
        top=LayoutUtils.dpToPx(8.33f,getResources());

		autoBorder();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (bitmap == null) {
			setBitmap();
		}
		canvas.drawBitmap(bitmap, 0, 0, null);
		SetMyWish(text, canvas);
	}

    /**
     * set background bitmap
     * @return
     */
	public Bitmap setBitmap() {
		bitmap = Bitmap.createBitmap((int) windowHeight, (int) windowWidth, Bitmap.Config.ARGB_8888);
		bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
		BitmapWidth = bitmap.getWidth();

		BitmapHeight = bitmap.getHeight();
		return bitmap;
	}
    /**
	 * 设置文字
	 * 
	 * @param content
	 */
	public void SetMyWish(String content, Canvas canvas) {

        /**
         * paint of shadow
         */
		paint1 = new Paint();
		paint1.setTextSize(size);
		paint1.setColor(shadowColor);
		paint1.setFakeBoldText(true);
		paint1.setAntiAlias(true);
		paint1.setTypeface(type);
		paint1.setStrokeWidth(4f);
		paint1.setStyle(Paint.Style.STROKE);
        /**
         * paint of text
         */
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
        String []texts=null;
        if(orientation==LinearLayout.VERTICAL)
            texts=autoSplitVertical(content, fm);
        else if(orientation==LinearLayout.HORIZONTAL)
		    texts = autoSplitHorizontal(content, windowWidth - 2* LayoutUtils.dpToPx(36.66f, getResources()));

		TextWidth = paint.measureText(texts[0]);
		TextHeight = texts.length * (baseline + fm.leading);
		/**
		 * draw text
		 */
		for (String t:texts) {
			canvas.drawText(t, x, y + y1, paint1); // 坐标以控件左上角为原点
			canvas.drawText(t, x, y + y1, paint); // 坐标以控件左上角为原点
			y1 += baseline + fm.leading; // 添加字体行间距
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        int fingerCount=event.getPointerCount();
        if(fingerCount>1){
            /**
             * zoom in
             */
            zoomIn(event,fingerCount);
            return true;
        }

		switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX() - TextWidth / 2;
                y = event.getY();
                autoBorder();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                x = event.getX() - TextWidth / 2;
                y = event.getY();
                autoBorder();
                invalidate();
                break;
            default:
                break;
		}
		return true;
	}


    /**
     * zoom in text
     * @param event
     * @param fingerCount
     */
    private void zoomIn(MotionEvent event,int fingerCount){
        if((event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && 2 == fingerCount) {
            int xLen = Math.abs((int)event.getX(0) - (int)event.getX(1));
            int yLen = Math.abs((int)event.getY(0) - (int)event.getY(1));

            nLenStart = Math.sqrt((double)xLen*xLen + (double)yLen * yLen);

            x = ((int)event.getX(0) + (int)event.getX(1))/2 - TextWidth / 2;
            y = ((int)event.getY(0) + (int)event.getY(1))/2 - TextHeight / 2;
            autoBorder();
            invalidate();
        }else if((event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE  && 2 ==fingerCount){
            int xLen = Math.abs((int)event.getX(0) - (int)event.getX(1));
            int yLen = Math.abs((int)event.getY(0) - (int)event.getY(1));

            double nLenEnd = Math.sqrt((double)xLen*xLen + (double)yLen * yLen);

            if(nLenEnd > nLenStart)//通过两个手指开始距离和结束距离，来判断放大缩小
            {
                //Toast.makeText(context, "放大", Toast.LENGTH_SHORT).show();
                setTextSize((int)(size*nLenEnd/nLenStart));
                Log.d("zhuchao","放大");
            }else{
                //Toast.makeText(context, "缩小", Toast.LENGTH_LONG).show();
                setTextSize((int)(size*nLenEnd/nLenStart));
                Log.d("zhuchao", "缩小");
            }
        }else if( (event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP  && 2 ==fingerCount) {

            int xLen = Math.abs((int)event.getX(0) - (int)event.getX(1));
            int yLen = Math.abs((int)event.getY(0) - (int)event.getY(1));

            double nLenEnd = Math.sqrt((double)xLen*xLen + (double)yLen * yLen);
            size=(int)(lastSize*nLenEnd/nLenStart);
            if(size>=150)
                size=150;
            if(size<=30)
                size=30;
            lastSize=size;
            setTextSize(size);
        }
    }
    /**
     * get text set horizontal
     * @param content
     * @param width
     * @return
     */
	private String[] autoSplitHorizontal(String content,float width) {
        int position=0;
		int length = content.length();
		float total_text_width = paint.measureText(content);
        float text_width=total_text_width/length;
		if (total_text_width <= width) {
			return new String[] { content };
		}
		int lines = (int) Math.ceil(total_text_width/ width); // 计算行数
		int rows=(int)(width/text_width);
		String[] lineTexts = new String[lines];
		for(int i=0;position<length&&i<lines;position+=rows,i++)
            if(position+rows>length)
                lineTexts[i]=content.substring(position);
            else
                lineTexts[i]=content.substring(position,position+rows);
		return lineTexts;
	}

	/**
	 * split content in vertical
	 * @param content
	 * @param fontMetrics
	 * @return
	 */
    private String[] autoSplitVertical(String content,FontMetrics fontMetrics){
        int length=content.length();
        if(length==1)
            return new String[]{content};
        float textHeight=length*(fontMetrics.descent-fontMetrics.ascent+fontMetrics.leading);
        int columns=(int)Math.ceil(textHeight/(windowHeight/2));

        int rows=(int)Math.ceil(length/columns);
        StringBuilder texts[]=new StringBuilder[rows];
        for(int i=0;i<rows;i++)
            texts[i]=new StringBuilder();
        for(int i=0;i<length;i++){
            int position=i%rows;
            texts[position].append(content.substring(i,i+1));
        }
        int maxLen=texts[0].length();
        for(int i=1;i<rows;i++) {
            if (texts[i].length() < maxLen)
                texts[i].append(" ", 0, 1);
        }
        String result[]=new String[rows];
        for(int i=0;i<rows;i++)
            result[i]=texts[i].toString();
        return result;
    }


	private void autoBorder() {
		if (left + BitmapWidth < windowWidth) {

			if (x >= left + BitmapWidth - TextWidth) {
				x = left + BitmapHeight - TextWidth;

			} else if (x < left) {
				x = left;
			}
		} else {
			if (x >= windowWidth - TextWidth) {// 右边界
				x = windowWidth - TextWidth;
			} else if (x <= left) {// 左边界
				x = left;
			}
		}
		if (top + BitmapHeight < windowHeight) {
			if (y >= windowHeight - TextHeight-LayoutUtils.dpToPx(180,getResources())) {
				y = windowHeight - TextHeight-LayoutUtils.dpToPx(180,getResources());
			} else if (y <= top) {
				y = top;
			}
		} else {
			if (y >= windowHeight - TextHeight-LayoutUtils.dpToPx(50,getResources())) {
				y = windowHeight - TextHeight-LayoutUtils.dpToPx(50,getResources());
			} else if (y <= top) {
				y = top;
			}
		}
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * get text orientation
	 * @return
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * set text orientation
	 * @param orientation
	 */
	public void setOrientation(int orientation) {
		this.orientation = orientation;
		x=left;
		y=top;
		invalidate();
	}

	/**
	 * get text color
	 * @return
	 */
	public int getTextColor() {
		return textColor;
	}

	/**
	 * set text color and update text style
	 * @param textColor
	 */
	public void setTextColor(int textColor) {
		this.textColor = textColor;
		invalidate();
	}

	/**
	 * get text style
	 * @return
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * set text style
	 * @param style
	 * @return
	 */
	public void setStyle(int style){
		this.style=style;
		invalidate();
	}
	/**
	 * set text shadow
	 * @return
	 */
	public int getShadowColor() {
		return shadowColor;
	}

	/**
	 * set shadow color
	 * @param shadowColor
	 */
	public void setShadowColor(int shadowColor) {
		this.shadowColor = shadowColor;
		invalidate();
	}
	public void setTextSize(int size) {
        if(size>=150)
            size=150;
        if(size<=30)
            size=30;
		this.size = size;
		autoBorder();
		invalidate();
	}
	public int getTextSize(){
		return size;
	}

	public void setType(Typeface type) {
		this.type = type;
		invalidate();
	}
	public Typeface getType(){
		return type;
	}

}
