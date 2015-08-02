package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.CommonAdapter;
import cn.luquba678.activity.adapter.StoryAdapter;
import cn.luquba678.activity.adapter.ViewHolder;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;
import cn.luquba678.entity.School;
import cn.luquba678.entity.TestDB;
import cn.luquba678.entity.User;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UniversityListActivity extends CommonActivity implements
		OnClickListener, OnItemClickListener, OnRefreshListener<ListView> {

	private ListView universityList;
	private cn.luquba678.utils.ImageLoader ima = new cn.luquba678.utils.ImageLoader(
			this);
	private List<School> schoolList;
	private CommonAdapter<School> adapter;
	private int page = 1;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			boolean hasMoreData = false;
			if (msg.obj != null) {
				hasMoreData = true;
				if (adapter == null) {
					adapter = new CommonAdapter<School>(self,
							(ArrayList<School>) msg.obj,
							R.layout.list_university_item) {
						@Override
						public void setViews(ViewHolder holder, School t, int p) {
							TextView count = holder
									.getView(R.id.grid_university_count);
							count.setText("排名:" + t.getRank());
							TextView university_name = holder
									.getView(R.id.university_name);
							university_name.setText(t.getSchool_name() + "");
							TextView university_area = holder
									.getView(R.id.university_area);
							university_area.setText("地区:" + t.getAreaName());
							ImageView logo = holder
									.getView(R.id.university_logo);
							ima.DisplayImage(t.getLogo(), logo, false);
							View mark_211 = holder.getView(R.id.is_211);
							View mark_985 = holder.getView(R.id.is_985);
							if (t.getIs_211() == 1) {
								mark_211.setVisibility(View.VISIBLE);
							} else
								mark_211.setVisibility(View.GONE);
							if (t.getIs_985() == 1)
								mark_985.setVisibility(View.VISIBLE);
							else
								mark_985.setVisibility(View.GONE);

						}
					};
					universityList.setAdapter(adapter);
					universityList
							.setOnItemClickListener(UniversityListActivity.this);

				} else {
					adapter.changeDateInThread((ArrayList<School>) msg.obj);
				}
			} else if (msg.what == 1) {
				Toast.makeText(self, "没有更多！", Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(self, "获取列表错误！", Toast.LENGTH_SHORT).show();

			}
			ptrlv.onPullDownRefreshComplete();
			ptrlv.onPullUpRefreshComplete();
			ptrlv.setHasMoreData(hasMoreData);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;
		case R.id.get_more:
			getSchoolList(++page, ADD);
			break;
		default:
			break;
		}
	}

	private PullToRefreshListView ptrlv;
	private SharedPreferences sharedPreferences;
	private Editor editor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_university_list);
		findViewById(R.id.top_back).setOnClickListener(this);
		((TextView) findViewById(R.id.top_text)).setText("学校排名");

		ptrlv = getView(R.id.universityList);
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(false);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		universityList = ptrlv.getRefreshableView();
		ptrlv.setOnRefreshListener(this);

		// universityList = (ListView) findViewById(R.id.universityList);

		getSchoolList(1, CHANGE);
		// schoolList = TestDB.getMatriculateMsg();
		// universityList.setAdapter(adapter);
		// universityList.setOnItemClickListener(this);
	}

	private final static int ADD = 0, CHANGE = 1;

	public void getSchoolList(final int page, final int type) {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					Message msg = new Message();
					JSONObject obj = JSONObject.parseObject(HttpUtil
							.postRequestEntity(Const.SCHOOL_QUERY + page, null));

					Integer errcode = obj.getInteger("errcode");
					msg.what = errcode;
					JSONArray arry = obj.getJSONArray("data");
					ArrayList<School> schoolListArry = School
							.getListFromJson(arry.toString());
					switch (type) {
					case CHANGE:
						schoolList = schoolListArry;
						break;
					default:
						if (schoolList != null) {
							schoolList.addAll(schoolListArry);
						} else {
							schoolList = schoolListArry;
						}
						break;
					}
					msg.obj = schoolList;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		School school = schoolList.get(arg2);

		MultipartEntity entity = new MultipartEntity();

		try {

			sharedPreferences = getSharedPreferences("luquba_login",
					Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();// 获取编辑器
			entity.addPart("school_id", new StringBody(school.getSchool_id()
					.toString()));
			entity.addPart(
					"stu_area_id",
					new StringBody(sharedPreferences.getString(
							School.HOME_AREA_ID, "")));

			LoadDataFromServer task = new LoadDataFromServer(
					Const.QUERY_SCHOOL_DETAIL, entity);
			task.getData(new DataCallBack() {

				private ArrayList<School> schoolListArry;
				private boolean hasMoreData = false;

				@Override
				public void onDataCallBack(int what, Object data) {
					if (what == 200) {
						JSONObject obj = JSONObject.parseObject(data.toString());
						Integer errcode = obj.getInteger("errcode");
						if (errcode == 0) {
							Intent intent = new Intent(self,
									UniversityDetailActivity.class);
							JSONObject school = obj.getJSONObject("school");
							JSONArray gradeline = obj.getJSONArray("gradeline");
							intent.putExtra("schoolJson", school.toJSONString());
							intent.putExtra("gradeline", gradeline.toJSONString());
							startActivity(intent);
						}
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		getSchoolList(1, CHANGE);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		getSchoolList(++page, ADD);

	}

}
