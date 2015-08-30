package com.zhuchao.view_rewrite;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/24/15.
 */
public class SelectPhotoDialog extends Dialog {
    private RelativeLayout rl_camera, rl_photo_album, rl_cancel;
    private View.OnClickListener onClickListener;
    public SelectPhotoDialog(Context context,View.OnClickListener clickListener) {
        super(context,android.R.style.Theme_Translucent);
        this.onClickListener=clickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_select_photo);

        rl_camera = (RelativeLayout)findViewById(R.id.rl_camer);
        rl_photo_album = (RelativeLayout)findViewById(R.id.rl_photo_album);
        rl_cancel = (RelativeLayout)findViewById(R.id.rl_cancle);

        rl_camera.setOnClickListener(onClickListener);
        rl_photo_album.setOnClickListener(onClickListener);
        rl_cancel.setOnClickListener(onClickListener);
    }
}
