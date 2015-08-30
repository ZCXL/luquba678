package com.zhuchao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zhuchao on 8/8/15.
 */
public class NetworkReceiver extends BroadcastReceiver {
    private boolean isConnected=false;
    public final static String NETWORK_CONNECT="CONNECT";
    public final static String NETWORK_DISCONNECT="DISCONNECT";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("zhuchao","1");
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null) {
            NetworkInfo[] networkInfos=connectivityManager.getAllNetworkInfo();
            for (int i = 0; i < networkInfos.length; i++) {
                NetworkInfo.State state=networkInfos[i].getState();
                if(NetworkInfo.State.CONNECTED==state) {
                    if(!isConnected) {
                        isConnected=true;
                        Toast.makeText(context, "已连接网络", Toast.LENGTH_SHORT).show();
                        /**
                         * send broadcast to tell activity that network is connected.
                         */
                        Intent intent1 = new Intent(NETWORK_CONNECT);
                        context.sendBroadcast(intent1);
                    }
                    return;
                }
            }
            if(isConnected) {
                /**
                 * send broadcast to tell activity that network is disconnected.
                 */
                isConnected = false;
                Intent intent1 = new Intent(NETWORK_DISCONNECT);
                context.sendBroadcast(intent1);
                Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
