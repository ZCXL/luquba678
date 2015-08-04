package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.ad;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.CommonAdapter;
import cn.luquba678.activity.adapter.StoryAdapter;
import cn.luquba678.activity.adapter.ViewHolder;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;
import cn.luquba678.entity.People;
import cn.luquba678.entity.News;
import cn.luquba678.entity.TestDB;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.ImageLoader;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshGridView;
import cn.luquba678.view.PullToRefreshListView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class PrettySchoolMateActivity extends CommonActivity implements
		OnClickListener, OnRefreshListener<GridView>, OnItemClickListener {

	private GridView mGrid;
	private CommonAdapter<News> adapter;
	private ImageLoader imageLoader;
	private TextView tv_pretty_girl, tv_pretty_boy;
	private RadioButton rb_pretty_girl, rb_pretty_boy;
	int page = 1;
	int sex = GIRL;
	final static int BOY = 1, GIRL = 2;
	private static ExecutorService nste = Executors.newSingleThreadExecutor();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;
		case R.id.pretty_boy:
			// adapter.changeDateInThread((ArrayList<Pretty>) TestDB.getBoys());
			setPretty(1, sex = BOY, 0);
			break;
		case R.id.pretty_girl:
			// adapter.changeDateInThread((ArrayList<Pretty>)
			// TestDB.getPeople());
			setPretty(1, sex = GIRL, 0);
			break;
		case R.id.pretty_girl_btn:
			rb_pretty_girl.performClick();
			break;
		case R.id.pretty_boy_btn:
			rb_pretty_boy.performClick();
			break;
		case R.id.get_more:
			// ++page;
			setPretty(page, sex, 1);
			break;
		default:
			break;
		}

	}

	private ArrayList<News> pretties;


	TextView getmore;
	private PullToRefreshGridView ptrlv;
	public static final String PRETTY = "校花校草";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pretty_school_mate);
		setOnClickLinstener(R.id.top_back, R.id.pretty_boy, R.id.pretty_girl,
				R.id.pretty_girl_btn, R.id.pretty_boy_btn);
		((TextView) findViewById(R.id.top_text)).setText(PRETTY);
		// mGrid = getView(R.id.pretty_school_mates);
		imageLoader = new ImageLoader(this);
		rb_pretty_girl = getView(R.id.pretty_girl);
		rb_pretty_boy = getView(R.id.pretty_boy);

		ptrlv = getView(R.id.pretty_school_mates);
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(true);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		mGrid = ptrlv.getRefreshableView();
		ptrlv.setOnRefreshListener(this);
		setPretty(1, GIRL, 0);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		setPretty(1, sex, 0);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		setPretty(++page, sex, 1);

	}

	private static final int PRETTYMATE = 3;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		News pretty = pretties.get(arg2);
		CommonNewsActivity.intentToDetailNews(pretty, self, PRETTYMATE);
	}

	public void setPretty(final int page, final int sex, final int action) {
		this.page = page;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					Message msg = new Message();
					String url = String.format(Const.PRETTY_QUERY, page, sex);
					Log.d("zhuchao",url);
					JSONObject obj = HttpUtil.getRequestJson(url, null);
					Integer errcode = obj.getInteger("errcode");
					msg.what = errcode;
					if (errcode == 0) {
						JSONArray arry = obj.getJSONArray("data");
						ArrayList<News> arrys = News.getListFromJson(arry
								.toString());
						if (arrys != null && arrys.size() > 0) {
							switch (action) {
							case 0:
								pretties = arrys;
								break;
							case 1:
								pretties.addAll(arrys);
								break;
							}
							msg.obj = pretties;
						}
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			boolean hasMoreData = false;
			if (msg.obj != null) {
				hasMoreData = true;
				if (adapter == null) {
					adapter = new CommonAdapter<News>(
							PrettySchoolMateActivity.this, pretties,
							R.layout.activity_pretty_school_mate_item) {

						@Override
						public void setViews(ViewHolder holder, News t,
								int position) {
							ImageView imageView = holder
									.getView(R.id.headImage);
							TextView name = holder.getView(R.id.name);
							String picUrl = t.getPic();
							imageView.setImageBitmap(null);
							imageLoader.DisplayImage(picUrl, imageView, false,350);
							name.setText(t.getTitle());
							// introduce.setText(t.getSchool());

						}

					};
					mGrid.setAdapter(adapter);
					mGrid.setOnItemClickListener(PrettySchoolMateActivity.this);
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

}
