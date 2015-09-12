package cn.luquba678.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CityMsg {
	// gb_id：地区标号，如：110000
	public int area_id;
	// area_name：地区全称，如：北京市
	public String area_name;
	// area_shortname：地区简称，如：北京
	public String area_shortname;

	// is_show：是否显示，如：1

	public CityMsg() {
		super();
	}

	public int getArea_id() {
		return area_id;
	}

	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getArea_shortname() {
		return area_shortname;
	}

	public void setArea_shortname(String area_shortname) {
		this.area_shortname = area_shortname;
	}

	public CityMsg getCityMsg(String json, Gson gson) {
		CityMsg u = gson.fromJson(json, CityMsg.class);
		return u;
	}

	/*
	 * final public static String citiesInfo =
	 * "[{\"gb_id\":\"110000\",\"area_name\":\"北京市\",\"area_shortname\":\"北京\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"120000\",\"area_name\":\"天津市\",\"area_shortname\":\"天津\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"130000\",\"area_name\":\"河北省\",\"area_shortname\":\"河北\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"140000\",\"area_name\":\"山西省\",\"area_shortname\":\"山西\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"150000\",\"area_name\":\"内蒙古自治区\",\"area_shortname\":\"内蒙古\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"210000\",\"area_name\":\"辽宁省\",\"area_shortname\":\"辽宁\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"220000\",\"area_name\":\"吉林省\",\"area_shortname\":\"吉林\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"230000\",\"area_name\":\"黑龙江省\",\"area_shortname\":\"黑龙江\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"310000\",\"area_name\":\"上海市\",\"area_shortname\":\"上海\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"320000\",\"area_name\":\"江苏省\",\"area_shortname\":\"江苏\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"330000\",\"area_name\":\"浙江省\",\"area_shortname\":\"浙江\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"340000\",\"area_name\":\"安徽省\",\"area_shortname\":\"安徽\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"350000\",\"area_name\":\"福建省\",\"area_shortname\":\"福建\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"360000\",\"area_name\":\"江西省\",\"area_shortname\":\"江西\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"370000\",\"area_name\":\"山东省\",\"area_shortname\":\"山东\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"410000\",\"area_name\":\"河南省\",\"area_shortname\":\"河南\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"420000\",\"area_name\":\"湖北省\",\"area_shortname\":\"湖北\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"430000\",\"area_name\":\"湖南省\",\"area_shortname\":\"湖南\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"440000\",\"area_name\":\"广东省\",\"area_shortname\":\"广东\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"450000\",\"area_name\":\"广西壮族自治区\",\"area_shortname\":\"广西\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"460000\",\"area_name\":\"海南省\",\"area_shortname\":\"海南\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"500000\",\"area_name\":\"重庆市\",\"area_shortname\":\"重庆\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"510000\",\"area_name\":\"四川省\",\"area_shortname\":\"四川\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"520000\",\"area_name\":\"贵州省\",\"area_shortname\":\"贵州\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"530000\",\"area_name\":\"云南省\",\"area_shortname\":\"云南\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"540000\",\"area_name\":\"西藏自治区\",\"area_shortname\":\"西藏\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"610000\",\"area_name\":\"陕西省\",\"area_shortname\":\"陕西\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"620000\",\"area_name\":\"甘肃省\",\"area_shortname\":\"甘肃\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"630000\",\"area_name\":\"青海省\",\"area_shortname\":\"青海\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"640000\",\"area_name\":\"宁夏回族自治区\",\"area_shortname\":\"宁夏\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"650000\",\"area_name\":\"新疆维吾尔自治区\",\"area_shortname\":\"新疆\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"710000\",\"area_name\":\"台湾省\",\"area_shortname\":\"台湾\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"810000\",\"area_name\":\"香港特别行政区\",\"area_shortname\":\"香港\",\"is_show\":\"1\"},"
	 * +
	 * "{\"gb_id\":\"910000\",\"area_name\":\"澳门特别行政区\",\"area_shortname\":\"澳门\",\"is_show\":\"1\"}]"
	 * ;
	 */
	final public static String citiesInfo = "["
			+ "{\"area_id\":\"0\",\"area_name\":\"不限\",\"area_shortname\":\"不限\"}"
			+ ",{\"area_id\":\"110000\",\"area_name\":\"\u5317\u4eac\u5e02\",\"area_shortname\":\"\u5317\u4eac\"}"
			+ ",{\"area_id\":\"120000\",\"area_name\":\"\u5929\u6d25\u5e02\",\"area_shortname\":\"\u5929\u6d25\"},{\"area_id\":\"130000\",\"area_name\":\"\u6cb3\u5317\u7701\",\"area_shortname\":\"\u6cb3\u5317\"},{\"area_id\":\"140000\",\"area_name\":\"\u5c71\u897f\u7701\",\"area_shortname\":\"\u5c71\u897f\"},{\"area_id\":\"150000\",\"area_name\":\"\u5185\u8499\u53e4\u81ea\u6cbb\u533a\",\"area_shortname\":\"\u5185\u8499\u53e4\"},{\"area_id\":\"210000\",\"area_name\":\"\u8fbd\u5b81\u7701\",\"area_shortname\":\"\u8fbd\u5b81\"},{\"area_id\":\"220000\",\"area_name\":\"\u5409\u6797\u7701\",\"area_shortname\":\"\u5409\u6797\"},{\"area_id\":\"230000\",\"area_name\":\"\u9ed1\u9f99\u6c5f\u7701\",\"area_shortname\":\"\u9ed1\u9f99\u6c5f\"},{\"area_id\":\"310000\",\"area_name\":\"\u4e0a\u6d77\u5e02\",\"area_shortname\":\"\u4e0a\u6d77\"},{\"area_id\":\"320000\",\"area_name\":\"\u6c5f\u82cf\u7701\",\"area_shortname\":\"\u6c5f\u82cf\"},{\"area_id\":\"330000\",\"area_name\":\"\u6d59\u6c5f\u7701\",\"area_shortname\":\"\u6d59\u6c5f\"},{\"area_id\":\"340000\",\"area_name\":\"\u5b89\u5fbd\u7701\",\"area_shortname\":\"\u5b89\u5fbd\"},{\"area_id\":\"350000\",\"area_name\":\"\u798f\u5efa\u7701\",\"area_shortname\":\"\u798f\u5efa\"},{\"area_id\":\"360000\",\"area_name\":\"\u6c5f\u897f\u7701\",\"area_shortname\":\"\u6c5f\u897f\"},{\"area_id\":\"370000\",\"area_name\":\"\u5c71\u4e1c\u7701\",\"area_shortname\":\"\u5c71\u4e1c\"},{\"area_id\":\"410000\",\"area_name\":\"\u6cb3\u5357\u7701\",\"area_shortname\":\"\u6cb3\u5357\"},{\"area_id\":\"420000\",\"area_name\":\"\u6e56\u5317\u7701\",\"area_shortname\":\"\u6e56\u5317\"},{\"area_id\":\"430000\",\"area_name\":\"\u6e56\u5357\u7701\",\"area_shortname\":\"\u6e56\u5357\"},{\"area_id\":\"440000\",\"area_name\":\"\u5e7f\u4e1c\u7701\",\"area_shortname\":\"\u5e7f\u4e1c\"},{\"area_id\":\"450000\",\"area_name\":\"\u5e7f\u897f\u58ee\u65cf\u81ea\u6cbb\u533a\",\"area_shortname\":\"\u5e7f\u897f\"},{\"area_id\":\"460000\",\"area_name\":\"\u6d77\u5357\u7701\",\"area_shortname\":\"\u6d77\u5357\"},{\"area_id\":\"500000\",\"area_name\":\"\u91cd\u5e86\u5e02\",\"area_shortname\":\"\u91cd\u5e86\"},{\"area_id\":\"510000\",\"area_name\":\"\u56db\u5ddd\u7701\",\"area_shortname\":\"\u56db\u5ddd\"},{\"area_id\":\"520000\",\"area_name\":\"\u8d35\u5dde\u7701\",\"area_shortname\":\"\u8d35\u5dde\"},{\"area_id\":\"530000\",\"area_name\":\"\u4e91\u5357\u7701\",\"area_shortname\":\"\u4e91\u5357\"},{\"area_id\":\"540000\",\"area_name\":\"\u897f\u85cf\u81ea\u6cbb\u533a\",\"area_shortname\":\"\u897f\u85cf\"},{\"area_id\":\"610000\",\"area_name\":\"\u9655\u897f\u7701\",\"area_shortname\":\"\u9655\u897f\"},{\"area_id\":\"620000\",\"area_name\":\"\u7518\u8083\u7701\",\"area_shortname\":\"\u7518\u8083\"},{\"area_id\":\"630000\",\"area_name\":\"\u9752\u6d77\u7701\",\"area_shortname\":\"\u9752\u6d77\"},{\"area_id\":\"640000\",\"area_name\":\"\u5b81\u590f\u56de\u65cf\u81ea\u6cbb\u533a\",\"area_shortname\":\"\u5b81\u590f\"},{\"area_id\":\"650000\",\"area_name\":\"\u65b0\u7586\u7ef4\u543e\u5c14\u81ea\u6cbb\u533a\",\"area_shortname\":\"\u65b0\u7586\"}," +
			"{\"area_id\":\"810000\",\"area_name\":\"香港特别行政区\",\"area_shortname\":\"香港\"}," +
			"{\"area_id\":\"710000\",\"area_name\":\"台湾省\",\"area_shortname\":\"台湾\"}," +
			"{\"area_id\":\"910000\",\"area_name\":\"澳门特别行政区\",\"area_shortname\":\"澳门\"}]";

	public static ArrayList<CityMsg> getCities() {
		Type listType = new TypeToken<ArrayList<CityMsg>>() {
		}.getType();
		Gson gson = new Gson();
		ArrayList<CityMsg> cities = gson.fromJson(citiesInfo, listType);
		return cities;
	}

	public static CityMsg getShortName(ArrayList<CityMsg> cities, String city) {
		if (cities == null) {
			cities = getCities();
		}
		CityMsg area = null;
		for (int i = 0; i < cities.size(); i++) {
			if (cities.get(i).getArea_name().equals(city)) {
				area = cities.get(i);
			}
		}
		return area;

	}

	public static CityMsg getShortNameFromId(int cityid) {

		ArrayList<CityMsg> cities = getCities();

		CityMsg area = null;
		for (int i = 0; i < cities.size(); i++) {
			if (cities.get(i).getArea_id() == cityid) {
				area = cities.get(i);
			}
		}
		return area;

	}

	public static CityMsg getAreaFromShortName(ArrayList<CityMsg> cities,
			String city) {
		if (cities == null) {
			cities = getCities();
		}
		CityMsg area = null;
		for (int i = 0; i < cities.size(); i++) {
			if (cities.get(i).getArea_shortname().equals(city)) {
				area = cities.get(i);
			}
		}
		return area;

	}
}
