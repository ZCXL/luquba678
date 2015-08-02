package cn.luquba678.utils.baidumap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import cn.luquba678.entity.Const;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.navisdk.util.common.StringUtils;

public abstract class LocationListener implements BDLocationListener {
	public abstract void setLocalAreaName(String province);

	@Override
	public void onReceiveLocation(BDLocation location) {
		String urlStr = Const.LOCAT_URL + "location=" + location.getLatitude()
				+ "," + location.getLongitude() + "&output=json&key="
				+ Const.LOCAT_KEY;

		new LoadDataFromServer(urlStr, true).getData(new DataCallBack() {
			@Override
			public void onDataCallBack(int what, Object data) {
				try {
					if (what == 200) {
						JSONObject jso = new JSONObject(data.toString());
						JSONObject result = jso.getJSONObject("result");
						JSONObject addressComponent = result
								.getJSONObject("addressComponent");
						String province = addressComponent
								.getString("province");
						setLocalAreaName(province);
					}
				} catch (JSONException e) {
					Log.e("jsonerror", e.getMessage());
				}
			}
		});
	}
}
