package cn.luquba678.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.utils.SPUtils;

public class PersonAccountDialog extends FullScreenDialog {

    private TextView phone,qq,weibo,wechat;
    private String type;
    public PersonAccountDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_account);
        initTitle(findViewById(R.id.person_accout_title), "我的账号");

        phone=(TextView)findViewById(R.id.phone_number);
        qq=(TextView)findViewById(R.id.QQ_number);
        weibo=(TextView)findViewById(R.id.microblog_number);
        wechat=(TextView)findViewById(R.id.wechat_number);
        type=(String)SPUtils.get(getContext(),"login_type","phone_number");


        if(type.equals("qq")) {
            qq.setText("Have bound");
            qq.setTextColor(Color.BLACK);
        }else if(type.equals("phone_number")) {
            phone.setText("Have bound");
            phone.setTextColor(Color.BLACK);
        }else if(type.equals("weibo")) {
            weibo.setText("Have bound");
            weibo.setTextColor(Color.BLACK);
        }else if(type.equals("wechat")) {
            wechat.setText("Have bound");
            wechat.setTextColor(Color.BLACK);
        }
    }
}
