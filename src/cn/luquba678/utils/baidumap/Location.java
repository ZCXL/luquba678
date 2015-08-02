package cn.luquba678.utils.baidumap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Location {
	public static BDLocation glocation = new BDLocation();
	public static String province = "";
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public BDLocationListener locationListener;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	public TextView reult;
	public Vibrator mVibrator;
	private Context context;

	public Location(BDLocationListener locationListener,
			LocationClient mLocationClient) {
		super();
		this.mLocationClient = mLocationClient;
		this.locationListener = locationListener;
		init();
	}

	public Location() {

	}

	public void init() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000 * 10;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(false);
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(locationListener);
		mGeofenceClient = new GeofenceClient(context);
		mVibrator = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
	}

	public void setLocationListener(BDLocationListener locationListener) {
		this.locationListener = locationListener;
	}

}