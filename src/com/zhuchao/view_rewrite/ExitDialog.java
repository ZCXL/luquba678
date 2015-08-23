package com.zhuchao.view_rewrite;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import cn.luquba678.R;
import cn.luquba678.activity.LoginActivity;

/**
 * Created by zhuchao on 8/20/15.
 */
public class ExitDialog extends Dialog implements View.OnClickListener{
    private Button cancel,confirm;
    private View root,sub;
    private Context context;
    public ExitDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        this.context=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_exit_dialog);

        cancel=(Button)findViewById(R.id.btn_cancel);
        confirm=(Button)findViewById(R.id.btn_ok);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        root=findViewById(R.id.root);
        sub=findViewById(R.id.sub);
        root.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < sub.getLeft() || event.getX() > sub.getRight() || event.getY() > sub.getBottom() || event.getY() < sub.getTop()) {
                    dismiss();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                Intent intent = new Intent(context, LoginActivity.class);
                loginOut();
                context.startActivity(intent);
                dismiss();
                ((Activity)context).finish();
                break;
        }
    }
    public void loginOut() {
        SharedPreferences sharedPreferences =context.getSharedPreferences("luquba_login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putString("login_token", "");
        editor.putString("uid", "");
        editor.commit();
    }

}
