package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonIOException;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.StoryAdapter;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.FamousSays;
import cn.luquba678.entity.News;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.ImageLoader;
import cn.luquba678.view.ImgScrollViewPager;
import cn.luquba678.view.NoScrollListView;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshListView;
import cn.luquba678.view.PullToRefreshNoScrollListView;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class SubMainActivity extends CommonActivity implements
		OnItemClickListener, OnRefreshListener<ListView> {

	private ListView listview;
	private TextView top_text, famous_says, famous_says_author;
	private ArrayList<View> listViews;
	private ImgScrollViewPager mmPager;
	private LinearLayout ovalLayout;
	private ImageLoader mImageLoader;
	private StoryAdapter adapter;
	private String url;
	private PullToRefreshListView ptrlv;
	private static final int LZGS = 1;
	private static final int ZYXD = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_main_page);
		// findViewById(R.id.top_back).setOnClickListener(this);
		setOnClickLinstener(R.id.top_back);
		top_text = getView(R.id.top_text);
		String top_textstr = getIntent().getStringExtra("title");
		top_text.setText(top_textstr);
		mmPager = (ImgScrollViewPager) findViewById(R.id.pager_ad);

		View fa = getView(R.id.famous_says_text_area);

		if ("励志故事".equals(top_textstr)) {
			url = Const.STORY_QUERY;
			type = LZGS;
			// task = new LoadDataFromServer(Const.CHAMPION_EXPERIENCE, true);
			getView(R.id.ad_relativeLayout).setVisibility(View.GONE);
			fa.setVisibility(View.VISIBLE);
			famous_says_author = getView(R.id.famous_says_author);
			famous_says = getView(R.id.famous_says_text);
			try {
				JSONObject obj = HttpUtil.getRequestJson(
						Const.QUERY_FAMOUS_SAYS, null);

				Integer errcode = obj.getInteger("errcode");
				if (errcode == 0) {
					JSONObject arry = obj.getJSONObject("data");
					FamousSays famousSays = FamousSays.getListFromJson(arry
							.toString());
					famous_says.setText(famousSays.getContent());
					famous_says_author.setText("——" + famousSays.getAuthor());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			type = ZYXD;
			getView(R.id.ad_relativeLayout).setVisibility(View.VISIBLE);
			fa.setVisibility(View.GONE);
			url = Const.CHAMPION_EXPERIENCE;
			// task = new LoadDataFromServer(Const.STORY_QUERY, true);
			initAds();
		}

		ptrlv = getView(R.id.listview_stories);
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(true);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		listview = ptrlv.getRefreshableView();
		setStory(page, 0);
		ptrlv.setOnRefreshListener(this);

	}

	private void initAds() {
		ovalLayout = (LinearLayout) findViewById(R.id.indicator);

		InitViewPager();// 初始化图片
		// 开始滚动
		mmPager.start(this, listViews, 4000, ovalLayout,
				R.layout.ad_bottom_item, R.id.ad_item_v,
				R.drawable.rectangle_focused, R.drawable.rectangle_normal);
	}

	private ArrayList<News> newsList;
	private int page = 1;
	private boolean hasMoreData = false;

	private void InitViewPager() {
		listViews = new ArrayList<View>();
		mImageLoader = new ImageLoader(this);
		String[] imageRes = new String[] {
				"http://a3.qpic.cn/psb?/V11it1sf1LpN71/tw1s.zTmxhv7NPhhyNWap44*Ej1v.iLvBEWZclJ7wY4!/b/dAgAAAAAAAAA&bo=0AKKAQAAAAADB3s!&rf=viewer_4",
				"http://a3.qpic.cn/psb?/V11it1sf1LpN71/FwNA4tZcuAg9mV3qoClRcj0X7Nv9swntRByyscuChM4!/b/dBQAAAAAAAAA&bo=qAJ0AQAAAAADAPo!&rf=viewer_4",
				"http://a3.qpic.cn/psb?/V11it1sf1LpN71/tw1s.zTmxhv7NPhhyNWap44*Ej1v.iLvBEWZclJ7wY4!/b/dAgAAAAAAAAA&bo=0AKKAQAAAAADB3s!&rf=viewer_4",
				"http://a3.qpic.cn/psb?/V11it1sf1LpN71/FwNA4tZcuAg9mV3qoClRcj0X7Nv9swntRByyscuChM4!/b/dBQAAAAAAAAA&bo=qAJ0AQAAAAADAPo!&rf=viewer_4" };

		for (int i = 0; i < imageRes.length; i++) {
			RelativeLayout relativeLayout = (RelativeLayout) View.inflate(this,
					R.layout.loop_ad_item, null);

			ImageView imageView = (ImageView) relativeLayout
					.findViewById(R.id.ad_image);
			// ImageView imageView = new ImageView(this);
			imageView.setOnClickListener(new ClickAd(i));
			mImageLoader.DisplayImage(imageRes[i], imageView, false);
			// imageView.setImageUrl(imageRes[i], imageLoader); //
			imageView.setScaleType(ScaleType.CENTER_CROP);
			listViews.add(relativeLayout);
		}
	}

	public void setStory(final int page, final int action) {
		this.page = page;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					String path = url+page;
					JSONObject obj = HttpUtil.getRequestJson(path, null);
					Integer errcode = obj.getInteger("errcode");
					Message msg = new Message();
					msg.what = errcode;
					if (errcode == 0) {
						JSONArray arry = obj.getJSONArray("data");
						if(arry!=null){
							
						
						ArrayList<News> arrys = News.getListFromJson(arry
								.toString());

						if (arrys != null && arrys.size() > 0) {
							hasMoreData = true;
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
					}
					handler.sendMessage(msg);}
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
					adapter = new StoryAdapter(self, (ArrayList<News>) msg.obj,
							R.layout.activity_story_cell);
					listview.setAdapter(adapter);
					listview.setOnItemClickListener(SubMainActivity.this);

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
	private int type;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;
		case R.id.get_more:
			setStory(++page, 1);
			break;
		}
	}

	class ClickAd implements OnClickListener {

		int id;

		public ClickAd(int id) {
			this.id = id;
		}

		public void onClick(View v) {// 设置图片点击事件
			Toast.makeText(SubMainActivity.this, "点击了." + id,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		News news = newsList.get(arg2);
		CommonNewsActivity.intentToDetailNews(news, self, type);

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		setStory(1, 0);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		setStory(++page, 1);

	}

}
