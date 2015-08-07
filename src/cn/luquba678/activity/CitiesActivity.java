package cn.luquba678.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.luquba678.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author zhy
 * 
 */
public class CitiesActivity extends Activity implements OnWheelChangedListener,
		OnClickListener {
	private JSONObject mJsonObj;
	private WheelView mProvince;
	private WheelView mCity;
	private WheelView mArea;
	private String[] mProvinceDatas;
	private TextView showChooseOK, showChooseCancle,choose_title;
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();
	public String mCurrentProvinceName;
	public String mCurrentCityName;
	public String mCurrentAreaName = "";

	private RelativeLayout confirm,cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose);
		initJsonData();
		mProvince = (WheelView) findViewById(R.id.id_province);
		mCity = (WheelView) findViewById(R.id.id_city);
		mArea = (WheelView) findViewById(R.id.id_area);
		showChooseOK = (TextView) findViewById(R.id.showChooseOK);
		showChooseOK.setOnClickListener(this);
		showChooseCancle = (TextView) findViewById(R.id.showChooseCancel);
		showChooseCancle.setOnClickListener(this);
		choose_title = (TextView) findViewById(R.id.choose_title);
		choose_title.setText("请选择家乡所在地");

		confirm=(RelativeLayout)findViewById(R.id.confirm_button);
		confirm.setOnClickListener(this);
		cancel=(RelativeLayout)findViewById(R.id.cancel_button);
		cancel.setOnClickListener(this);


		initDatas();

		mProvince.setViewAdapter(new ArrayWheelAdapter<String>(this,
				mProvinceDatas));
		mProvince.addChangingListener(this);
		mCity.addChangingListener(this);
		mArea.addChangingListener(this);

		mProvince.setVisibleItems(5);
		mCity.setVisibleItems(5);
		mArea.setVisibleItems(5);
		updateCities();
		updateAreas();

	}

	private void updateAreas() {
		int pCurrent = mCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProvinceName)[pCurrent];
		String[] areas = mAreaDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		ArrayWheelAdapter<String> adaper =new ArrayWheelAdapter<String>(this, areas,16);
		mArea.setViewAdapter(adaper);
		mArea.setCurrentItem(0);
	}

	private void updateCities() {
		int pCurrent = mProvince.getCurrentItem();
		mCurrentProvinceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProvinceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mCity.setCurrentItem(5);
		updateAreas();
	}

	private void initDatas() {
		try {
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonP = jsonArray.getJSONObject(i);
				String province = jsonP.getString("p");

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try {
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("c");
				} catch (Exception e1) {
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++) {
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("n");
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try {
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("a");
					} catch (Exception e) {
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];
					for (int k = 0; k < jsonAreas.length(); k++) {
						String area = jsonAreas.getJSONObject(k).getString("s");
						mAreasDatas[k] = area;
					}
					mAreaDatasMap.put(city, mAreasDatas);
				}

				mCitisDatasMap.put(province, mCitiesDatas);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		mJsonObj = null;
	}

	private void initJsonData() {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				sb.append(new String(buf, 0, len, "gbk"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mProvince) {
			updateCities();
		} else if (wheel == mCity) {
			updateAreas();
		} else if (wheel == mArea) {
			mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.showChooseOK:
				int position = mArea.getCurrentItem();
				Intent intent = new Intent();
				intent.putExtra("proviceName", mCurrentProvinceName);
				intent.putExtra("cityName", mCurrentCityName);
				if (mAreaDatasMap.get(mCurrentCityName) != null) {
					intent.putExtra("areaName",
							mAreaDatasMap.get(mCurrentCityName)[position]);
				}
				setResult(5, intent);
				finish();
				break;
			case R.id.showChooseCancel:
				finish();
				break;
			case R.id.confirm_button:
				position = mArea.getCurrentItem();
				intent = new Intent();
				intent.putExtra("proviceName", mCurrentProvinceName);
				intent.putExtra("cityName", mCurrentCityName);
				if (mAreaDatasMap.get(mCurrentCityName) != null) {
					intent.putExtra("areaName",
							mAreaDatasMap.get(mCurrentCityName)[position]);
				}
				setResult(5, intent);
				finish();
				break;
			case R.id.cancel_button:
				finish();
				break;
			default:
				break;
		}
	}

}
