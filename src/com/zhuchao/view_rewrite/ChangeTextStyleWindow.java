package com.zhuchao.view_rewrite;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/14/15.
 */
public class ChangeTextStyleWindow extends PopupWindow implements View.OnClickListener{

    private RelativeLayout text_style,text_font,text_color,text_size,text_oritention;
    private Context context;
    private View view;
    private TextView textView;

    private View.OnClickListener clickListener;
    public ChangeTextStyleWindow(Context context,View.OnClickListener clickListener){
        super(context);
        this.context=context;
        this.clickListener=clickListener;
        initView();
    }
    private void initView(){
        view= LayoutInflater.from(context).inflate(R.layout.change_text_style,null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.style_left);
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);


        /**
         * init view and set listener
         */
        text_style=(RelativeLayout)view.findViewById(R.id.text_style);
        if(clickListener==null)
            text_style.setOnClickListener(this);
        else
            text_style.setOnClickListener(clickListener);
        text_font=(RelativeLayout)view.findViewById(R.id.text_font);
        if(clickListener==null)
            text_font.setOnClickListener(this);
        else
            text_font.setOnClickListener(clickListener);
        text_color=(RelativeLayout)view.findViewById(R.id.text_color);
        if(clickListener==null)
            text_color.setOnClickListener(this);
        else
            text_color.setOnClickListener(clickListener);
        text_size=(RelativeLayout)view.findViewById(R.id.text_size);
        if(clickListener==null)
            text_size.setOnClickListener(this);
        else
            text_size.setOnClickListener(clickListener);
        text_oritention=(RelativeLayout)view.findViewById(R.id.text_orientation);
        if(clickListener==null)
            text_oritention.setOnClickListener(this);
        else
            text_oritention.setOnClickListener(clickListener);

        textView=(TextView)view.findViewById(R.id.orientation_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_style:
                break;
            case R.id.text_font:
                break;
            case R.id.text_color:
                break;
            case R.id.text_size:
                break;
            case R.id.text_orientation:
                break;
        }
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public void setText_oritention(String text){
        textView.setText(text);
    }
}
