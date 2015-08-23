package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cn.luquba678.R;
import cn.luquba678.activity.adapter.SchoolListAdapter;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.School;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshListView;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;

public class QueryResultActivity extends CommonActivity implements OnClickListener, OnItemClickListener, OnRefreshListener<ListView> {

	private ListView schoolListView;

	private SchoolListAdapter adapter;

	private MultipartEntity entity;
	private PullToRefreshListView ptrlv;

	private ArrayList<School> schools=new ArrayList<School>();

	private int page = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_result);
		findViewById(R.id.top_back).setOnClickListener(this);

		dialog = new ProgressDialog(this);
		dialog.setMessage("正在查询...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		ptrlv = getView(R.id.universityList);
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(true);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		schoolListView = ptrlv.getRefreshableView();
		ptrlv.setOnRefreshListener(this);

		entity = new MultipartEntity();
		try {
			Intent intent = getIntent();
			String grade = intent.getStringExtra(School.GRADE);
			String stu_area_id = intent.getStringExtra(School.HOME_AREA_ID);
			String school_area_id = intent.getStringExtra(School.SCHOOL_AREA_ID);
			String kelei = intent.getStringExtra(School.KELEI);
			entity.addPart("stu_area_id", new StringBody(stu_area_id, Charset.forName("UTF-8")));
			entity.addPart("school_area_id", new StringBody(school_area_id, Charset.forName("UTF-8")));
			entity.addPart("kelei",new StringBody(kelei, Charset.forName("UTF-8")));
			entity.addPart("grade",new StringBody(grade, Charset.forName("UTF-8")));
			getSchool(page, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getSchool(int page, int action) {
		boolean hasMoreData = false;
		try {

			String jsonData = HttpUtil.postRequestEntity(Const.QUERY_SCHOOL + page, entity);
			if (StringUtils.isNotEmpty(jsonData)) {
				JSONObject obj = JSONObject.parseObject(jsonData);
				Integer errCode = obj.getInteger("errcode");
				if (errCode == 0) {
					JSONArray arry = obj.getJSONArray("data");
					ArrayList<School> arrys = School.getListFromJson(arry.toString());
					if (hasMoreData = (arrys != null && arrys.size() > 0)) {
						switch (action) {
						case CHANGE:
							schools.clear();
							schools.addAll(arrys);
							QueryResultActivity.this.page++;
							break;
						case ADD:
							schools.addAll(arrys);
							QueryResultActivity.this.page++;
							break;
						}
						if (adapter == null) {
							adapter = new SchoolListAdapter(this, schools, R.layout.query_result_item);
							schoolListView.setAdapter(adapter);
						} else {
							adapter.changeDateInThread(schools);
						}
					} else {
						toast("亲,没有可刷新的了!");
					}
				} else {
					toast("没有查询到可上学校！");
				}
				dialog.dismiss();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ptrlv.onPullDownRefreshComplete();
		ptrlv.onPullUpRefreshComplete();
		ptrlv.setHasMoreData(hasMoreData);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		getSchool(page=1, CHANGE);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		page++;
		getSchool(page,ADD);

	}

	private final static int ADD = 0, CHANGE = 1;

}
