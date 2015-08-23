package com.zhuchao.view_rewrite;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.luquba678.R;
import cn.luquba678.activity.MainFragmentActivity;

/**
 * Created by zhuchao on 8/19/15.
 */
public class VersionCheckDialog extends Dialog implements View.OnClickListener{
    private TextView version_id,version_intro;
    private Button cancel,confirm;
    private View root,sub;
    private String id,intro,download_url;
    public VersionCheckDialog(Context context,String id,String intro,String downloadr_url) {
        super(context,R.style.CustomProgressDialog);
        this.id=id;
        this.intro=intro;
        this.download_url=downloadr_url;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_version);

        version_id=(TextView)findViewById(R.id.version_id);
        version_id.setText("版本号:V"+id);
        version_intro=(TextView)findViewById(R.id.version_intro);
        version_intro.setText(intro);
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
                if(MainFragmentActivity.downloadService!=null)
                    MainFragmentActivity.downloadService.addDownloadTask(download_url, 1);
                dismiss();
                break;
        }
    }
}
