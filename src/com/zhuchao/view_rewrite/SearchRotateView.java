package com.zhuchao.view_rewrite;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhuchao.utils.LayoutUtils;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/13/15.
 */
public class SearchRotateView extends CustomerView {
    /**
     * attributes
     */
    private int imageSource;
    private int rippleColor;
    private int rippleSpeed;
    private int rotateSpeed;
    private boolean isRotate;
    private int direction=1;

    /**
     * image view
     */
    private ImageView imageView;
    /**
     * circle radius
     */
    private int radius;
    /**
     * rotate degree
     */
    private float degree;

    /**
     * image scale
     */
    private float scale;
    private int scaleDirection;
    /**
     * search button bitmap
     */
    private Bitmap bitmap;

    public SearchRotateView(Context context) {
        super(context);
    }

    public SearchRotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }

    @Override
    void setAttributes(AttributeSet attributes) {
        setBackgroundResource(android.R.color.transparent);

        /**
         * get attributes of view
         */
        TypedArray array=getContext().obtainStyledAttributes(attributes,R.styleable.searchRotateView);

        imageSource=array.getResourceId(R.styleable.searchRotateView_image_source,R.drawable.ic_launcher);
        int color=array.getColor(R.styleable.searchRotateView_ripple_color,-1);
        if(color!=-1)
            rippleColor=color;
        else{
            int colorId=array.getResourceId(R.styleable.searchRotateView_ripple_color,0xffffff);
            rippleColor=getContext().getResources().getColor(colorId);
        }

        rippleSpeed=array.getInteger(R.styleable.searchRotateView_ripple_speed, 2);
        rotateSpeed=array.getInteger(R.styleable.searchRotateView_rotate_speed, 2);
        isRotate=array.getBoolean(R.styleable.searchRotateView_is_rotate, false);

        /**
         * add image view into layout
         */
        imageView=new ImageView(getContext());
        imageView.setImageResource(imageSource);
        LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.setMargins(LayoutUtils.dpToPx(15, getResources()), LayoutUtils.dpToPx(15, getResources()), LayoutUtils.dpToPx(15, getResources()), LayoutUtils.dpToPx(15, getResources()));
        params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        imageView.setLayoutParams(params);

        addView(imageView);

        /**
         * set new layout params
         */
        degree=0;
        scale=1.0f;
        scaleDirection=1;
        radius=imageView.getWidth()/2;
        /**
         * get image view bitmap
         */
        bitmap=BitmapFactory.decodeResource(getResources(), imageSource);
    }


    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(isRotate){
            //drawImage();
            drawCircle(canvas);
        }
    }

    /**
     * start search animation
     */
    public void startSearch(){
        isRotate=true;
        invalidate();
    }

    /**
     * end search animation
     */
    public void endSearch(){
        isRotate=false;
        invalidate();
    }

    /**
     * draw ripple circle
     * @param canvas
     */
    private void drawCircle(Canvas canvas){
        Bitmap bitmap= Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp=new Canvas(bitmap);
        Paint paint=new Paint();
        paint.setColor(makePressColor());
        paint.setAntiAlias(true);
        temp.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

        Paint paint1 = new Paint();
        paint1.setColor(getResources().getColor(android.R.color.transparent));
        paint1.setAntiAlias(true);
        paint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        temp.drawCircle(getWidth() / 2, getHeight() / 2, imageView.getWidth() / 2, paint1);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());

        if(radius<getWidth()/2&&direction==1){
            radius+=rippleSpeed;
            if(radius>=getWidth()/2) {
                direction = -1;
                radius-=rippleSpeed;
            }
        }else if(radius<getWidth()/2&&direction==-1){
            radius-=rippleSpeed;
            if(radius<=imageView.getWidth()/2) {
                direction = 1;
                radius+=rippleSpeed;
            }
        }
        invalidate();
    }

    /**
     * draw image
     */
    private void drawImage(){
        Matrix matrix=new Matrix();
        //matrix.postRotate(degree);
        matrix.postScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(newBitmap);

        if(scale-1.3f<1e-5&&scaleDirection==1){
            scale+=0.01f;
            if(scale-1.3f>=1e-5){
                scale-=0.01;
                scaleDirection=-1;
            }
        }else if(scale-1.3f<1e-5&&scaleDirection==-1){
            scale-=0.01f;
            if(scale-1.0f<=1e-5){
                scale+=0.01;
                scaleDirection=1;
            }
        }

//        degree+=rotateSpeed;
//        if(degree<=360)
//            return;
//        else
//            degree=0;
    }
    protected int makePressColor(){
        int r = (this.rippleColor >> 16) & 0xFF;
        int g = (this.rippleColor >> 8) & 0xFF;
        int b = (this.rippleColor>> 0) & 0xFF;
        return Color.argb(128, r, g, b);
    }

    public int getImageSource() {
        return imageSource;
    }

    public void setImageSource(int imageSource) {
        this.imageSource = imageSource;
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
    }

    public int getRippleSpeed() {
        return rippleSpeed;
    }

    public void setRippleSpeed(int rippleSpeed) {
        this.rippleSpeed = rippleSpeed;
    }

    public int getRotateSpeed() {
        return rotateSpeed;
    }

    public void setRotateSpeed(int rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    public boolean isRotate() {
        return isRotate;
    }

    public void setIsRotate(boolean isRotate) {
        this.isRotate = isRotate;
    }
}
