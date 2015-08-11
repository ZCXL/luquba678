package com.zhuchao.view_rewrite;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/10/15.
 */
public class ConnectNetDialog extends Dialog {
    public ConnectNetDialog(Context context) {
        super(context);
    }

    public static class Builder implements View.OnClickListener{
        private Context context;
        private Button quit,connect;
        private ConnectNetDialog connectNetDialog;
        public Builder(Context context){
            this.context=context;
        }
        public ConnectNetDialog Create(){
            View view= LayoutInflater.from(context).inflate(R.layout.network_notification,null);
            connectNetDialog=new ConnectNetDialog(context);
            quit=(Button)view.findViewById(R.id.quit);
            connect=(Button)view.findViewById(R.id.connect);
            quit.setOnClickListener(this);
            connect.setOnClickListener(this);
            connectNetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            connectNetDialog.setCancelable(false);
            connectNetDialog.setCanceledOnTouchOutside(false);
            connectNetDialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            connectNetDialog.setContentView(view);
            connectNetDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            return connectNetDialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.quit:
                    connectNetDialog.dismiss();
                    break;
                case R.id.connect:
                    Intent intent = null;
                    try {
                        String sdkVersion = android.os.Build.VERSION.SDK;
                        if (Integer.valueOf(sdkVersion) > 10) {
                            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        } else {
                            intent = new Intent(); ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        context.startActivity(intent);
                        connectNetDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
