package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuchao.http.Network;
import com.zhuchao.utils.ImageLoader;
import com.zhuchao.view_rewrite.LoadingDialog;
import cn.luquba678.R;
import cn.luquba678.activity.adapter.CommonAdapter;
import cn.luquba678.activity.adapter.ViewHolder;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.School;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshListView;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UniversityListActivity extends CommonActivity implements OnClickListener, OnItemClickListener, OnRefreshListener<ListView> {

	private ListView universityList;
	private ImageLoader ima = new ImageLoader(this);
	private ArrayList<School> schoolList;
	private CommonAdapter<School> adapter;
	private int page = 1;
	private LoadingDialog loadingDialog;

	private PullToRefreshListView ptrlv;
	private SharedPreferences sharedPreferences;
	private Editor editor;

	private RelativeLayout error_layout;
	private Button refresh_button;

	private final static int ADD = 0, CHANGE = 1;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0:
					adapter.changeDateInThread(schoolList);
					break;
				case 1:
					Toast.makeText(self, "没有更多！", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(self, "获取列表错误！", Toast.LENGTH_SHORT).show();
					break;
			}
			ptrlv.onPullDownRefreshComplete();
			ptrlv.onPullUpRefreshComplete();
			ptrlv.setHasMoreData(true);
			loadingDialog.stopProgressDialog();
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_university_list);

		initView();
	}
	private void initView(){
		loadingDialog=new LoadingDialog(this);
		ima.setStub_id(R.drawable.new_city_default);
		findViewById(R.id.top_back).setOnClickListener(this);
		((TextView) findViewById(R.id.top_text)).setText("学校排名");

		/**
		 * network error bg
		 */
		error_layout=(RelativeLayout)findViewById(R.id.network_error);
		refresh_button=(Button)findViewById(R.id.network_error_button);
		refresh_button.setOnClickListener(this);

		ptrlv = getView(R.id.universityList);
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(false);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		universityList = ptrlv.getRefreshableView();
		ptrlv.setOnRefreshListener(this);


		schoolList=new ArrayList<School>();
		adapter = new CommonAdapter<School>(self,schoolList , R.layout.list_university_item) {
			@Override
			public void setViews(ViewHolder holder, School t, int p) {
				TextView count = holder.getView(R.id.grid_university_count);
				count.setText("排名:" + t.getRank());
				TextView university_name = holder.getView(R.id.university_name);
				university_name.setText(t.getSchool_name() + "");
				TextView university_area = holder.getView(R.id.university_area);
				university_area.setText("地区:" + t.getAreaName());
				ImageView logo = holder.getView(R.id.university_logo);
				ima.DisplayImage(t.getLogo(), logo);
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
		universityList.setOnItemClickListener(this);
		getSchoolList(page,ADD);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.top_back:
				this.finish();
				break;
			case R.id.get_more:
				page++;
				getSchoolList(page, ADD);
				break;
			case R.id.network_error_button:
				getSchoolList(page,ADD);
				break;
			default:
				break;
		}
	}

	public void getSchoolList(final int page, final int type) {
		if(Network.checkNetWorkState(UniversityListActivity.this)) {
			if(error_layout.getVisibility()==View.VISIBLE)
				error_layout.setVisibility(View.INVISIBLE);
            loadingDialog.startProgressDialog();
			Executors.newSingleThreadExecutor().execute(
					new Runnable() {
						@Override
						public void run() {
							try {
								JSONObject obj = JSONObject.parseObject(HttpUtil.postRequestEntity(Const.SCHOOL_QUERY + page, null));
								Integer err_code = obj.getInteger("errcode");
								if (err_code ==0) {
									JSONArray array = obj.getJSONArray("data");
									ArrayList<School> schoolListArray = School.getListFromJson(array.toString());
									switch (type) {
										case CHANGE:
											schoolList.clear();
											schoolList.addAll(schoolListArray);
											break;
										case ADD:
											schoolList.addAll(schoolListArray);
											break;
									}
									handler.sendEmptyMessage(0);
								}else{
									handler.sendEmptyMessage(1);
								}
							}catch(Exception e){
								handler.sendEmptyMessage(2);
								e.printStackTrace();
							}
						}
					}
			);
		}else{
			/**
			 * send network broadcast
			 */
			Toast.makeText(UniversityListActivity.this,"未连接网络",Toast.LENGTH_SHORT).show();
			if(error_layout.getVisibility()==View.INVISIBLE)
				error_layout.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		School school = schoolList.get(arg2);

		MultipartEntity entity = new MultipartEntity();

		try {

			sharedPreferences = getSharedPreferences("luquba_login",Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();// 获取编辑器
			entity.addPart("school_id", new StringBody(school.getSchool_id().toString()));
			entity.addPart("stu_area_id", new StringBody(sharedPreferences.getString(School.HOME_AREA_ID, "110000")));

			LoadDataFromServer task = new LoadDataFromServer(Const.QUERY_SCHOOL_DETAIL, entity);
			task.getData(new DataCallBack() {

				@Override
				public void onDataCallBack(int what, Object data) {
					if (what == 200) {
						JSONObject obj = JSONObject.parseObject(data.toString());
						Integer errcode = obj.getInteger("errcode");
						if (errcode == 0) {
							Intent intent = new Intent(self, UniversityDetailActivity.class);
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
		page++;
		getSchoolList(page, ADD);

	}

}
