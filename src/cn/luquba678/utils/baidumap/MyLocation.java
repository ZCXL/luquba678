package cn.luquba678.utils.baidumap;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.service.SourceUrl;

public class MyLocation {
	private final static String key = "F9da85afead8b6e9c4738e5e5b79eb97";

	public void getJsonLocation(String latValue, String longValue,
			final TextView tv) {
		String urlStr = "http://api.map.baidu.com/geocoder?location="
				+ latValue + "," + longValue + "&output=json&key=" + key;
		new LoadDataFromServer(urlStr, null).getData(new DataCallBack() {
			@Override
			public void onDataCallBack(int what, Object data) {
				try {
					if (tv != null) {
						JSONObject jso = new JSONObject(data.toString());
						JSONObject result = jso.getJSONObject("result");
						JSONObject addressComponent = result
								.getJSONObject("addressComponent");
						String province = addressComponent
								.getString("province");
						tv.setVisibility(View.INVISIBLE);
						System.out.println(province);
						tv.setText(province);
						tv.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					Log.e("jsonerror", e.getMessage());
				}
			}
		});
	}
}
