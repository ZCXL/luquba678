package com.zhuchao.view_rewrite;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zhuchao.utils.FileSystem;
import com.zhuchao.utils.ImageProcess;

import cn.luquba678.R;
import cn.luquba678.utils.ToolUtils;

/**
 * Created by zhuchao on 8/19/15.
 */
public class ClearCacheDialog extends Dialog implements View.OnClickListener{
    private TextView cache;
    private Button cancel,confirm;
    private View root,sub;
    private Context context;
    public ClearCacheDialog(Context context) {
        super(context, R.style.CustomProgressDialog);

        this.context=context;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.clear_cache);
        cache=(TextView)findViewById(R.id.number_cache);
        cache.setText(String.valueOf(FileSystem.getFolderSize())+"M");
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
                ImageProcess.DeleteImage();
                ToolUtils.showShortToast(context, "缓存已清除");
                dismiss();
                break;
        }
    }
}
