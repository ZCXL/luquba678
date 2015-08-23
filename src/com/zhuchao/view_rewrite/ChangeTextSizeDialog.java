package com.zhuchao.view_rewrite;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


import com.zhuchao.utils.LayoutUtils;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/15/15.
 */
public class ChangeTextSizeDialog extends AlertDialog implements Slider.OnValueChangedListener{
    private int size;
    private OnSizeChangeListener onSizeChangeListener;
    private Slider slider;
    public ChangeTextSizeDialog(Context context){
        super(context);
    }
    public ChangeTextSizeDialog(Context context,int theme,int size){
        super(context, theme);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_text_size);

        slider=(Slider)findViewById(R.id.text_size);
        slider.setOnValueChangedListener(this);
        slider.setValue(size);

        Window window=this.getWindow();
        WindowManager.LayoutParams params=window.getAttributes();
        window.setGravity(Gravity.BOTTOM|Gravity.CENTER);
        params.x=0;
        params.y= LayoutUtils.dpToPx(100,getContext().getResources());

        window.setAttributes(params);
    }
    /**
     * set text size
     * @param size
     */
    public void setSize(int size){
        this.size=size;
    }

    public OnSizeChangeListener getOnSizeChangeListener() {
        return onSizeChangeListener;
    }

    public void setOnSizeChangeListener(OnSizeChangeListener onSizeChangeListener) {
        this.onSizeChangeListener = onSizeChangeListener;
    }

    @Override
    public void onValueChanged(int value) {
        this.size=value;
        if(onSizeChangeListener!=null)
            onSizeChangeListener.onSizeChange(value);
    }

    public interface OnSizeChangeListener{
        void onSizeChange(int value);
    }
}
