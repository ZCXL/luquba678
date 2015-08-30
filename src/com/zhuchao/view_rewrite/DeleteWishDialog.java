package com.zhuchao.view_rewrite;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.zhuchao.bean.Wish;
import com.zhuchao.http.Network;
import java.util.concurrent.Executors;

import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

/**
 * Created by zhuchao on 8/20/15.
 */
public class DeleteWishDialog extends Dialog implements View.OnClickListener{
    private Button cancel,confirm;
    private View root,sub;
    private Context context;
    private Wish wish;
    private int position;
    private LoadingDialog loadingDialog;
    private OnDeleteListener onDeleteListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(onDeleteListener!=null)
                        onDeleteListener.onSuccess(position);
                    break;
                case 2:
                    if(onDeleteListener!=null)
                        onDeleteListener.onFail();
                    break;
            }
            loadingDialog.stopProgressDialog();
        }
    };
    public DeleteWishDialog(Context context) {
        super(context,android.R.style.Theme_Translucent);
        this.context=context;
        loadingDialog=new LoadingDialog(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wish_delete);
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
    public void show(Wish wish,int position){
        this.wish=wish;
        this.position=position;
        super.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                delete();
                dismiss();
                break;
        }
    }
    public void delete(){
        if(Network.checkNetWorkState(context)) {
            loadingDialog.startProgressDialog();
            Executors.newSingleThreadExecutor().execute(new Runnable() {

                @Override
                public void run() {
                    String deleteWishUrl = String.format(Const.DELETE_WORD, User.getUID(context), User.getLoginToken(context));
                    MultipartEntity entity = new MultipartEntity();
                    String list="[\""+wish.getId()+"\"]";
                    try {
                        entity.addPart("list", new StringBody(list));
                        JSONObject obj= HttpUtil.getRequestJson(deleteWishUrl, entity);
                        int err_code=obj.getIntValue("errcode");
                        if(err_code==0){
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(2);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else{
            handler.sendEmptyMessage(2);
        }
    }

    public OnDeleteListener getOnDeleteListener() {
        return onDeleteListener;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    /**
     * delete interface
     */
    public interface OnDeleteListener{
        void onSuccess(int position);
        void onFail();
    }
}
