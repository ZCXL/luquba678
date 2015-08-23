package com.zhuchao.view_rewrite;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import cn.luquba678.R;
import cn.luquba678.ui.FullScreenDialog;

/**
 * Created by zhuchao on 8/21/15.
 */
public class AboutUs extends FullScreenDialog implements android.view.View.OnClickListener{
    public AboutUs(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        getView(R.id.top_back).setOnClickListener(this);
        TextView topText = getView(R.id.top_text);
        topText.setText("关于我们");
        WebView wv= getView(R.id.content);
        wv.loadUrl("file:///android_asset/about.html");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}
