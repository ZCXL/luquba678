package com.zhuchao.view_rewrite;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.zhuchao.gif.GifListener;
import com.zhuchao.gif.GifView;

import cn.luquba678.R;

public class CustomProgressDialog extends Dialog implements GifListener{
	private Context context = null;
    private static CustomProgressDialog customProgressDialog = null;
    //private GifView gifView;
	public CustomProgressDialog(Context context){
        super(context);
        this.context = context;
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        this.setContentView(R.layout.customerprogressdialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        initView();
    }
	public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        this.setContentView(R.layout.customerprogressdialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        //initView();
    }
	public static CustomProgressDialog createDialog(Context context){
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);

        return customProgressDialog;
    }
    private void initView(){
//        gifView=(GifView)findViewById(R.id.loading_animation);
//        gifView.setGifImage(R.drawable.loading_animation);
//        gifView.setLoopAnimation();
//        gifView.setListener(this, 2);
    }
    public void dismiss(){
//        gifView.pauseGifAnimation();
//        gifView.destroy();
        super.dismiss();
    }
    public void onWindowFocusChanged(boolean hasFocus){
        if (customProgressDialog == null){
            return;
        }
        CircularProgressBar circularProgressBar = (CircularProgressBar) customProgressDialog.findViewById(R.id.customer_progress_dialog_progressBar);
        circularProgressBar.startAnimation();
    }
    public CustomProgressDialog setTitile(String strTitle){
        return customProgressDialog;
    }
    public CustomProgressDialog setMessage(String strMessage){
        return customProgressDialog;
    }

    @Override
    public void gifEnd(int num) {

    }

    @Override
    public void frameCount(int frame) {

    }
}
