package com.zhuchao.view_rewrite;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zhuchao.utils.ImageProcess;

/**
 * Created by zhuchao on 8/30/15.
 */
public class LargeBitmapImage extends ImageView {
    public LargeBitmapImage(Context context) {
        super(context);
    }

    public LargeBitmapImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LargeBitmapImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        //bm= ImageProcess.compressImage(bm,100);
        super.setImageBitmap(bm);
    }
}
