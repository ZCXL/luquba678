package cn.luquba678.activity.fragment;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.navisdk.util.common.StringUtils;
import com.zhuchao.http.Network;

import cn.luquba678.R;
import cn.luquba678.activity.CityChooserActivity;
import cn.luquba678.activity.QueryResultActivity;
import cn.luquba678.activity.listener.BackChangeOnTouchListener;
import cn.luquba678.entity.CityMsg;
import cn.luquba678.entity.School;
import cn.luquba678.utils.baidumap.LocationListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: TabCFm
 * @Description: TODO
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:47
 *
 */
public class QueryFragment extends Fragment implements OnClickListener, OnCheckedChangeListener {
	//score edit
	private EditText etScore;
	//type of school
	private RadioGroup keleiGroup;
	private RelativeLayout etHomePlace, etSchoolPlace;
	private BackChangeOnTouchListener back;
	private int keleiChoosed;
	private TextView query_school_place_school;
	private TextView query_home_place_home;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private LocationClient mLocationClient;
	private LocationListener mMyLocationListener;

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

		/**
		 * init all view
		 */
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
		back = new BackChangeOnTouchListener(R.drawable.frame_radius8_alfa8_black, R.drawable.frame_radius8_alfa0_black);
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
		int span = 5000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mMyLocationListener = new LocationListener() {

			@Override
			public void setLocalAreaName(String province) {
				if (StringUtils.isNotEmpty(province)) {
                    Log.i("定位省份", province);
					locat_area = CityMsg.getShortName(null, province);
					query_home_place_home.setText(locat_area.getArea_shortname());
					mLocationClient.stop();
				}

			}

		};
		mLocationClient.registerLocationListener(mMyLocationListener);
		mLocationClient.start();
	}

	@Override
	public void onClick(View v) {
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
			if(!Network.checkNetWorkState(getActivity())) {
				Toast.makeText(getActivity(), "未连接网络", Toast.LENGTH_SHORT).show();
				break;
			}
			sharedPreferences = getActivity().getSharedPreferences("luquba_login", Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();// 获取编辑器

			String score = etScore.getText().toString();
			String kelei = "1";
			String home_area = query_home_place_home.getText().toString();
			String school_area = query_school_place_school.getText().toString();

			if ("".equals(score) || score == null) {
				Toast.makeText(this.getActivity(), "同学，请告诉我你考了多少分呀？", Toast.LENGTH_SHORT).show();
				etScore.selectAll();
				break;
			} else if (Double.parseDouble(score) > getGrade() || Double.parseDouble(score) < 0) {
				Toast.makeText(this.getActivity(), "同学，你在逗我吗？", Toast.LENGTH_SHORT).show();
				etScore.selectAll();
				break;
			}
			if ("".equals(score) || score == null) {
				Toast.makeText(this.getActivity(), "同学，请告诉我你考了多少分呀？", Toast.LENGTH_SHORT).show();
				etScore.selectAll();
				break;
			}
			if (keleiChoosed == R.id.search_wenke) {
				kelei = "2";
			}
			home_provence = CityMsg.getAreaFromShortName(null, home_area);
			if(home_provence==null){
				Toast.makeText(this.getActivity(), "No location info", Toast.LENGTH_SHORT).show();
				break;
			}
			String home_id = home_provence.getArea_id() + "";
			school_provence = CityMsg.getAreaFromShortName(null, school_area);
			String school_area_id = "";
			if (school_provence != null) {
				school_area_id = school_provence.getArea_id() + "";
			}


			editor.putString(School.GRADE, score);
			editor.putString(School.HOME_AREA_ID, home_id);
			editor.putString(School.KELEI, kelei);
			editor.commit();// 提交修改

			Intent intenti = new Intent(QueryFragment.this.getActivity(), QueryResultActivity.class);
			intenti.putExtra(School.GRADE, score);
			intenti.putExtra(School.HOME_AREA_ID, home_id);
			intenti.putExtra(School.SCHOOL_AREA_ID, school_area_id);
			intenti.putExtra(School.KELEI, kelei);
			QueryFragment.this.startActivity(intenti);
			break;
		default:
			break;
		}
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
	private int getGrade(){
		String province=query_home_place_home.getText().toString();
		if(province.equals("上海"))
			return 630;
		if(province.equals("江苏"))
			return 480;
		if(province.equals("浙江"))
			return 810;
		if(province.equals("海南"))
			return 900;
		return 750;
	}
}
