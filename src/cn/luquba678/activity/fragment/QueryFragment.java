package cn.luquba678.activity.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.navisdk.util.common.StringUtils;
import com.google.gson.JsonObject;

import cn.luquba678.R;
import cn.luquba678.activity.CityChooserActivity;
import cn.luquba678.activity.QueryResultActivity;
import cn.luquba678.activity.listener.BackChangeOnTouchListener;
import cn.luquba678.entity.CityMsg;
import cn.luquba678.entity.News;
import cn.luquba678.entity.School;
import cn.luquba678.entity.User;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.MatriculateMsgService;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.utils.baidumap.LocationListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @ClassName: TabCFm
 * @Description: TODO
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:47
 *
 */
public class QueryFragment extends Fragment implements OnItemClickListener,
		OnClickListener, OnCheckedChangeListener {
	private EditText etScore;
	private RadioGroup keleiGroup;
	private RelativeLayout etHomePlace, etSchoolPlace;
	private BackChangeOnTouchListener back;
	private int keleiChoosed;
	private TextView query_school_place_school;
	private TextView query_home_place_home;
	private LoadDataFromServer registerTask;
	private ProgressDialog dialog;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private LocationClient mLocationClient;
	private LoadDataFromServer loadDate;
	private ListView cityList;
	private ArrayList<CityMsg> cities;
	private EditText citySearch;
	private TextView local_address, locat_hint;
	private LocationListener mMyLocationListener;
	private GeofenceClient mGeofenceClient;
	private Vibrator mVibrator;
	private boolean isLocated = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_main_query, container, false);
	}

	public View findViewById(int id) {
		return this.getView().findViewById(id);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findViewById(R.id.top_back).setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.top_text)).setText("查询");
		etScore = (EditText) findViewById(R.id.query_score);
		keleiGroup = (RadioGroup) findViewById(R.id.query_kelei);
		keleiGroup.setOnCheckedChangeListener(this);
		etHomePlace = (RelativeLayout) findViewById(R.id.query_home_place);
		etSchoolPlace = (RelativeLayout) findViewById(R.id.query_school_place);
		etHomePlace.setOnClickListener(this);
		etSchoolPlace.setOnClickListener(this);
		back = new BackChangeOnTouchListener(
				R.drawable.frame_radius8_alfa8_black,
				R.drawable.frame_radius8_alfa0_black);
		etHomePlace.setOnTouchListener(back);
		etSchoolPlace.setOnTouchListener(back);
		query_home_place_home = (TextView) findViewById(R.id.query_home_place_home);
		query_home_place_home.setText("请选择考试地点");
		query_school_place_school = (TextView) findViewById(R.id.query_school_place_school);
		query_school_place_school.setText("不限");
		findViewById(R.id.do_search).setOnClickListener(this);

		location();

	}

	private CityMsg locat_area, home_provence, school_provence;
	private SharedPreferences sharedPreferences;
	private Editor editor;

	private void location() {
		mLocationClient = new LocationClient(this.getActivity());
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
				if (StringUtils.isNotEmpty(province)) {
					// locat_hint.setVisibility(View.GONE);
					Log.i("定位省份", province);
					locat_area = CityMsg.getShortName(null, province);
					query_home_place_home.setText(locat_area
							.getArea_shortname());

					// query_home_place_home.setVisibility(View.VISIBLE);
					isLocated = true;
					mLocationClient.stop();
				}

			}

		};
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getActivity()
				.getApplicationContext());
		mVibrator = (Vibrator) getActivity().getApplicationContext()
				.getSystemService(Service.VIBRATOR_SERVICE);
		mLocationClient.start();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	@Override
	public void onClick(View v) {
		LoadDataFromServer loadDate = null;
		Intent intent;
		switch (v.getId()) {
		case R.id.query_home_place:
			intent = new Intent(this.getActivity(), CityChooserActivity.class);
			intent.putExtra("type", 1);
			startActivityForResult(intent, 1);
			break;
		case R.id.query_school_place:
			intent = new Intent(this.getActivity(), CityChooserActivity.class);
			intent.putExtra("type", 2);
			startActivityForResult(intent, 2);
			break;
		case R.id.do_search:
			sharedPreferences = getActivity().getSharedPreferences(
					"luquba_login", Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();// 获取编辑器

			String score = etScore.getText().toString();
			String kelei = "1";
			String home_area = query_home_place_home.getText().toString();
			String school_area = query_school_place_school.getText().toString();

			if ("".equals(score) || score == null) {
				Toast.makeText(this.getActivity(), "同学，请告诉我你考了多少分呀？", 0).show();
				etScore.selectAll();
				break;
			} else if (Double.parseDouble(score) > 900
					|| Double.parseDouble(score) < 0) {
				Toast.makeText(this.getActivity(), "同学，你在逗我吗？", 0).show();
				etScore.selectAll();
				break;
			}
			if ("".equals(score) || score == null) {
				Toast.makeText(this.getActivity(), "同学，请告诉我你考了多少分呀？", 0).show();
				etScore.selectAll();
				break;
			}
			if (keleiChoosed == R.id.search_wenke) {
				kelei = "2";
			}

			home_provence = CityMsg.getAreaFromShortName(null, home_area);
			String home_id = home_provence.getArea_id() + "";
			school_provence = CityMsg.getAreaFromShortName(null, school_area);
			String school_area_id = "";
			if (school_provence != null) {
				school_area_id = school_provence.getArea_id() + "";
			}

			loadDate = MatriculateMsgService
					.getMatriculateMsgListLoadDataFromServer(home_id,
							school_area_id, kelei, score);
			editor.putString(School.GRADE, score);
			editor.putString(School.HOME_AREA_ID, home_id);
			editor.putString(School.KELEI, kelei);
			editor.commit();// 提交修改

			Intent intenti = new Intent(QueryFragment.this.getActivity(),
					QueryResultActivity.class);
			intenti.putExtra(School.GRADE, score);
			intenti.putExtra(School.HOME_AREA_ID, home_id);
			intenti.putExtra(School.SCHOOL_AREA_ID, school_area_id);
			intenti.putExtra(School.KELEI, kelei);
			QueryFragment.this.startActivity(intenti);
			/*
			 * loadDate.getData(new DataCallBack() {
			 * 
			 * @Override public void onDataCallBack(int what, Object data) { if
			 * (what == 200 && data != null) { dialog.dismiss(); Intent intent =
			 * new Intent(QueryFragment.this .getActivity(),
			 * QueryResultActivity.class); com.alibaba.fastjson.JSONObject obj =
			 * com.alibaba.fastjson.JSONObject.parseObject(data.toString());
			 * com.alibaba.fastjson.JSONArray arry = obj.getJSONArray("data");
			 * intent.putExtra("jsonData", arry.toJSONString());
			 * QueryFragment.this.startActivity(intent); } else {
			 * dialog.dismiss();
			 * Toast.makeText(QueryFragment.this.getActivity(), "网络错误",
			 * 0).show(); }
			 * 
			 * }
			 * 
			 * });
			 */
			break;
		default:
			break;
		}
	}

	/**
	 * 根据条件加载博客
	 */
	public void flushBlogs(int i) {
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		keleiChoosed = group.getCheckedRadioButtonId();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 200:
			String area_name = data.getStringExtra("area_name");
			switch (requestCode) {
			case 1:
				query_home_place_home.setText(area_name);
				break;
			case 2:
				query_school_place_school.setText(area_name);
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}

	}

}
