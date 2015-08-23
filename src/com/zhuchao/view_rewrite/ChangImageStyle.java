package com.zhuchao.view_rewrite;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhuchao.utils.ImageBeauty;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/16/15.
 */
public class ChangImageStyle extends Dialog implements View.OnClickListener{

    private OnImageChangeStyleListener onImageChangeStyleListener;
    private Bitmap bitmap;
    private View backView,view;
    private RelativeLayout convertToRoundedCorner,convertToBlackWhite,convertToBlur,convertToSketch,sharpenImageAmeliorate,recover;
    public ChangImageStyle(Context context) {
        super(context);
    }

    public ChangImageStyle(Context context, int theme) {
        super(context, android.R.style.Theme_Translucent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_image_style);

        backView=findViewById(R.id.backView);
        view=findViewById(R.id.rootSelector);
        convertToRoundedCorner=(RelativeLayout)findViewById(R.id.convertToRoundedCorner);
        convertToRoundedCorner.setOnClickListener(this);
        convertToBlackWhite=(RelativeLayout)findViewById(R.id.convertToBlackWhite);
        convertToBlackWhite.setOnClickListener(this);
        convertToBlur=(RelativeLayout)findViewById(R.id.convertToBlur);
        convertToBlur.setOnClickListener(this);
        convertToSketch=(RelativeLayout)findViewById(R.id.convertToSketch);
        convertToSketch.setOnClickListener(this);
        sharpenImageAmeliorate=(RelativeLayout)findViewById(R.id.sharpenImageAmeliorate);
        sharpenImageAmeliorate.setOnClickListener(this);
        recover=(RelativeLayout)findViewById(R.id.recover);
        recover.setOnClickListener(this);

        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < backView.getLeft() || event.getX() > backView.getRight() || event.getY() >backView.getBottom() || event.getY() < backView.getTop()) {
                    dismiss();
                }
                return false;
            }
        });
    }

    public void show(Bitmap bitmap){
        this.bitmap=bitmap;
        super.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.convertToRoundedCorner:
                if(bitmap!=null){
                    bitmap= ImageBeauty.convertToRoundedCorner(bitmap,5.0f);
                    if(onImageChangeStyleListener!=null)
                        onImageChangeStyleListener.onImageChangeStyle(bitmap);
                }
                dismiss();
                break;
            case R.id.convertToBlackWhite:
                if(bitmap!=null){
                    bitmap= ImageBeauty.convertToBlackWhite(bitmap);
                    if(onImageChangeStyleListener!=null)
                        onImageChangeStyleListener.onImageChangeStyle(bitmap);
                }
                dismiss();
                break;
            case R.id.convertToBlur:
                if(bitmap!=null){
                    bitmap= ImageBeauty.convertToBlur(bitmap);
                    if(onImageChangeStyleListener!=null)
                        onImageChangeStyleListener.onImageChangeStyle(bitmap);
                }
                dismiss();
                break;
            case R.id.convertToSketch:
                if(bitmap!=null){
                    bitmap= ImageBeauty.convertToSketch(bitmap);
                    if(onImageChangeStyleListener!=null)
                        onImageChangeStyleListener.onImageChangeStyle(bitmap);
                }
                dismiss();
                break;
            case R.id.sharpenImageAmeliorate:
                if(bitmap!=null){
                    bitmap= ImageBeauty.sharpenImageAmeliorate(bitmap);
                    if(onImageChangeStyleListener!=null)
                        onImageChangeStyleListener.onImageChangeStyle(bitmap);
                }
                dismiss();
                break;
            case R.id.recover:
                if(bitmap!=null){
                    if(onImageChangeStyleListener!=null)
                        onImageChangeStyleListener.onImageChangeStyle(bitmap);
                }
                dismiss();
                break;
        }
    }

    public OnImageChangeStyleListener getOnImageChangeStyleListener() {
        return onImageChangeStyleListener;
    }

    public void setOnImageChangeStyleListener(OnImageChangeStyleListener onImageChangeStyleListener) {
        this.onImageChangeStyleListener = onImageChangeStyleListener;
    }

    public interface OnImageChangeStyleListener{
        void onImageChangeStyle(Bitmap bitmap);
    }

    // 用于在UI线程中更新界面
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b,PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
            }
        }
    }
    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }
}
