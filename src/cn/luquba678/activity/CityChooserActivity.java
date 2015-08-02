package cn.luquba678.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.navisdk.util.common.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.CityListAdapter;
import cn.luquba678.entity.CityMsg;
import cn.luquba678.entity.Const;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.SourceUrl;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.utils.CacheUtil;
import cn.luquba678.utils.baidumap.LocationListener;
import cn.luquba678.utils.baidumap.MyLocation;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CityChooserActivity extends Activity implements OnClickListener,
		OnItemClickListener, TextWatcher {
	private CityListAdapter adapter;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private LocationClient mLocationClient;
	private LoadDataFromServer loadDate;
	private ListView cityList;
	private ArrayList<CityMsg> cities,allcity;
	private EditText citySearch;
	private TextView local_address, locat_hint;
	private LocationListener mMyLocationListener;
	private GeofenceClient mGeofenceClient;
	private Vibrator mVibrator;
	private boolean isLocated = false;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_city);
		type = getIntent().getIntExtra("type", 1);
		cityList = (ListView) findViewById(R.id.city_list);
		findViewById(R.id.top_back).setOnClickListener(this);
		findViewById(R.id.local_chooser).setOnClickListener(this);
		citySearch = (EditText) findViewById(R.id.top_text);
		citySearch.addTextChangedListener(this);
		loadDate = new LoadDataFromServer(SourceUrl.GET_Area_Url, null);
		local_address = (TextView) findViewById(R.id.local_address);
		locat_hint = (TextView) findViewById(R.id.locat_hint);
		// local_address.setText(getResources().getText(R.string.locating));
		local_address.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isLocated) {
					mLocationClient.stop();
				}
			}
		});
		location();

		cities = CityMsg.getCities();
		if(type==1){
			cities.remove(0);
		}
		allcity=cities;
		adapter = new CityListAdapter(CityChooserActivity.this, cities,
				R.layout.query_city_item);
		cityList.setAdapter(adapter);
		cityList.setOnItemClickListener(CityChooserActivity.this);

	}

	private void location() {
		mLocationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000 * 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(false);
		mLocationClient.setLocOption(option);
		mMyLocationListener = new LocationListener() {
			@Override
			public void setLocalAreaName(String province) {
				if (StringUtils.isNotEmpty(province) && local_address != null) {
					locat_hint.setVisibility(View.GONE);
					local_address.setText(province);
					local_address.setVisibility(View.VISIBLE);
					isLocated = true;
				}

			}

		};
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);
		mLocationClient.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;
		case R.id.local_chooser:
			if (!isLocated) {
				Toast.makeText(this, "还未定位成功，请稍等...", 0).show();
			} else {
				String areaName = CityMsg.getShortName(cities,
						local_address.getText().toString()).getArea_shortname();
				findViewById(R.id.is_choosed_local).setVisibility(View.VISIBLE);
				Intent intent = new Intent();
				intent.putExtra("area_name", areaName);
				this.setResult(200, intent);
				this.finish();
			}

			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String areaName = cities.get(position).area_shortname;
		view.findViewById(R.id.is_choosed).setVisibility(View.VISIBLE);
		Intent intent = new Intent();
		intent.putExtra("area_name", areaName);
		this.setResult(200, intent);
		this.finish();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		ArrayList<CityMsg> cis = new ArrayList<CityMsg>();
		if (!"".equals(s) && s != null)
			for (CityMsg ci : allcity) {
				if (ci.area_name.contains(s)) {
					cis.add(ci);
				}
			}
		if (cis == null || cis.size() == 0) {
			cis = allcity;
		}
		cities = cis;
		adapter.changeDate(cis);

	}

	@Override
	public void afterTextChanged(Editable s) {
	}

}
