package cn.luquba678.utils;

import android.content.Context;
import android.content.Intent;

import com.zhuchao.view_rewrite.ConnectNetDialog;

public class Until {
	/**
	 * send connect broadcast
	 * @param context
	 */
	public static void sendNetworkBroadcast(Context context){
		Intent intent=new Intent("android.net.conn.CONNECTIVITY_CHANGE");
		context.sendBroadcast(intent);
	}

	/**
	 * show connect network dialog
	 * @param context
	 */
	public static void showConnectNetDialog(Context context){
		new ConnectNetDialog.Builder(context).Create().show();
	}
}