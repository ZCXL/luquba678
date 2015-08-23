package cn.luquba678.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.utils.SPUtils;

public class PersonAccountDialog extends FullScreenDialog {

    private TextView phone,qq,weibo,wechat;
    private String type;
    private ImageView back_image;
    private LinearLayout back;
    public PersonAccountDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_account);


        back_image=(ImageView)findViewById(R.id.title_top_image);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        back=(LinearLayout)findViewById(R.id.top_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        phone=(TextView)findViewById(R.id.phone_number);
        qq=(TextView)findViewById(R.id.QQ_number);
        weibo=(TextView)findViewById(R.id.microblog_number);
        wechat=(TextView)findViewById(R.id.wechat_number);
        type=(String)SPUtils.get(getContext(),"login_type","phone_number");


        if(type.equals("qq")) {
            qq.setText("已绑定");
            qq.setTextColor(Color.BLACK);
        }else if(type.equals("phone_number")) {
            phone.setText("已绑定");
            phone.setTextColor(Color.BLACK);
        }else if(type.equals("weibo")) {
            weibo.setText("已绑定");
            weibo.setTextColor(Color.BLACK);
        }else if(type.equals("wechat")) {
            wechat.setText("已绑定");
            wechat.setTextColor(Color.BLACK);
        }
    }
}
