package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.StoryAdapter;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshListView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FunnyActivity extends CommonActivity implements
		OnItemClickListener, OnRefreshListener<ListView> {
	private RadioButton rb_funny_school_btn, rb_funny_image_btn, rb_jokes_btn;
	private ListView listview;
	private StoryAdapter adapter;
	int page, type = 1;
	public static int XYQS=4;
	public static int GXDZ=5;
	public static int NHT=6;
	int detailType = XYQS;
	

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		setFunny(0, type, 0);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		addFunny();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;
		case R.id.funny_school:
			page = 1;
			type = 1;
			detailType = XYQS;
			setFunny(page, type, 0);
			break;
		case R.id.funny_image:
			page = 1;
			detailType = NHT;
			type = 3;
			setFunny(page, type, 0);
			break;
		case R.id.jokes:
			page = 1;
			detailType = GXDZ;
			type = 2;
			setFunny(page, type, 0);
			break;
		case R.id.funny_school_btn:
			rb_funny_school_btn.performClick();
			break;
		case R.id.funny_image_btn:
			rb_funny_image_btn.performClick();
			break;
		case R.id.jokes_btn:
			rb_jokes_btn.performClick();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_funny);
		initView();

	}

	private PullToRefreshListView ptrlv;

	private void initView() {
		setOnClickLinstener(R.id.top_back, R.id.funny_school_btn,
				R.id.funny_image_btn, R.id.jokes_btn, R.id.funny_school,
				R.id.funny_image, R.id.jokes);
		rb_funny_school_btn = getView(R.id.funny_school);
		rb_funny_image_btn = getView(R.id.funny_image);
		rb_jokes_btn = getView(R.id.jokes);
		((TextView) findViewById(R.id.top_text)).setText("轻松一刻");

		// listview_stories = (ListView) findViewById(R.id.funnylistview);
		ptrlv = getView(R.id.funnylistview);
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(true);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		listview = ptrlv.getRefreshableView();
		ptrlv.setOnRefreshListener(this);
		setFunny(0, 1, 0);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			boolean hasMoreData = false;
			if (msg.obj != null && msg.what == 0) {
				hasMoreData = true;
				if (adapter == null) {
					adapter = new StoryAdapter(self, (ArrayList<News>) msg.obj,
							R.layout.activity_funny_cell);
					listview.setAdapter(adapter);
					listview.setOnItemClickListener(FunnyActivity.this);
				} else {
					adapter.changeDateInThread((ArrayList<News>) msg.obj);
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
	private ArrayList<News> newsList;

	public void setFunny(final int page, final int type, final int action) {
		this.page = page;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					String url = String.format(Const.FUNNY_QUERY, page, type);
					JSONObject obj = HttpUtil.getRequestJson(url, null);
					Integer errcode = obj.getInteger("errcode");
					Message msg = new Message();
					msg.what = errcode;
					if (errcode == 0) {
						JSONArray arry = obj.getJSONArray("data");
						ArrayList<News> arrys = News.getListFromJson(arry
								.toString());

						if (arrys != null && arrys.size() > 0) {
							switch (action) {
							case 0:
								newsList = arrys;
								break;
							case 1:
								newsList.addAll(arrys);
								break;
							}

						}
						msg.obj = newsList;
						/*
						 * newsList.addAll(News.getListFromJson(arry
						 * .toString()));
						 * storyAdapter.changeDateInThread(newsList);
						 */
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		News news = newsList.get(arg2);
		FunnyDetailActivity.intentToDetailNews(news, self,detailType);

	}

	public void addFunny() {
		setFunny(++page, type, 1);
	}
}
