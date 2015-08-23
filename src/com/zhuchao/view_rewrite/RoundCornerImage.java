package com.zhuchao.view_rewrite;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zhuchao.utils.ImageProcess;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/21/15.
 */
public class RoundCornerImage extends ImageView {
    private Context mContext;
    private int radius=15;
    public RoundCornerImage(Context context) {
        super(context);
        mContext = context;
    }

    public RoundCornerImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCustomAttributes(attrs);
    }

    public RoundCornerImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setCustomAttributes(attrs);
    }

    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray array=mContext.obtainStyledAttributes(attrs,R.styleable.RoundCornerImage);
        radius=array.getInteger(R.styleable.RoundCornerImage_radius,15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable() ;
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class)
            return;
        Bitmap bitmap = ImageProcess.zoomImage(((BitmapDrawable) drawable).getBitmap(),getWidth(),getHeight());
        Bitmap roundBitmap = toRoundCorner(bitmap,radius);
        canvas.drawBitmap(roundBitmap,0,0,new Paint());
    }

    /**
     * 获取圆角位图的方法
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, getWidth(),getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
