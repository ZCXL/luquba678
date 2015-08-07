package com.zhuchao.connection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.zhuchao.service.DownloadService;

import cn.luquba678.activity.MainFragmentActivity;


public class DownloadServiceConnection implements ServiceConnection {
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		MainFragmentActivity.downloadService=((DownloadService.DownloadServiceBinder)service).getService();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
       MainFragmentActivity.downloadService=null;
	}

}
