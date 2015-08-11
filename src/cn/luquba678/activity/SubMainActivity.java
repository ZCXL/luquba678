package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuchao.http.Network;
import com.zhuchao.view_rewrite.LoadingDialog;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.StoryAdapter;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.FamousSays;
import cn.luquba678.entity.News;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.ImageLoader;
import cn.luquba678.utils.Until;
import cn.luquba678.view.ImgScrollViewPager;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshListView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class SubMainActivity extends CommonActivity implements OnItemClickListener, OnRefreshListener<ListView> {

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

	private LoadingDialog loadingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_main_page);

		initView();

	}

	private void initView(){
		loadingDialog=new LoadingDialog(SubMainActivity.this);

		setOnClickLinstener(R.id.top_back);
		top_text = getView(R.id.top_text);
		String top_text_str = getIntent().getStringExtra("title");
		top_text.setText(top_text_str);
		mmPager = (ImgScrollViewPager) findViewById(R.id.pager_ad);

		View fa = getView(R.id.famous_says_text_area);

		if ("励志故事".equals(top_text_str)) {
			url = Const.STORY_QUERY;
			type = LZGS;

			getView(R.id.ad_relativeLayout).setVisibility(View.GONE);
			fa.setVisibility(View.VISIBLE);
			famous_says_author = getView(R.id.famous_says_author);
			famous_says = getView(R.id.famous_says_text);
			try {
				JSONObject obj = HttpUtil.getRequestJson(
						Const.QUERY_FAMOUS_SAYS, null);

				Integer errcode = obj.getInteger("errcode");
				if (errcode == 0) {
					JSONObject array = obj.getJSONObject("data");
					FamousSays famousSays = FamousSays.getListFromJson(array.toString());

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
    /**
     * init advertisement
     */
	private void initAds() {
		ovalLayout = (LinearLayout) findViewById(R.id.indicator);

		InitViewPager();// 初始化图片
		// 开始滚动
		mmPager.start(this, listViews, 4000, ovalLayout, R.layout.ad_bottom_item, R.id.ad_item_v, R.drawable.rectangle_focused, R.drawable.rectangle_normal);
	}

	private ArrayList<News> newsList=new ArrayList<News>();
	private int page = 1;
	private boolean hasMoreData = false;

    /**
     * init view pager
     */
	private void InitViewPager() {
		listViews = new ArrayList<View>();
		mImageLoader = new ImageLoader(this);
		String[] imageRes = new String[]
				{"http://a3.qpic.cn/psb?/V11it1sf1LpN71/tw1s.zTmxhv7NPhhyNWap44*Ej1v.iLvBEWZclJ7wY4!/b/dAgAAAAAAAAA&bo=0AKKAQAAAAADB3s!&rf=viewer_4",
				"http://a3.qpic.cn/psb?/V11it1sf1LpN71/FwNA4tZcuAg9mV3qoClRcj0X7Nv9swntRByyscuChM4!/b/dBQAAAAAAAAA&bo=qAJ0AQAAAAADAPo!&rf=viewer_4",
				"http://a3.qpic.cn/psb?/V11it1sf1LpN71/tw1s.zTmxhv7NPhhyNWap44*Ej1v.iLvBEWZclJ7wY4!/b/dAgAAAAAAAAA&bo=0AKKAQAAAAADB3s!&rf=viewer_4",
				"http://a3.qpic.cn/psb?/V11it1sf1LpN71/FwNA4tZcuAg9mV3qoClRcj0X7Nv9swntRByyscuChM4!/b/dBQAAAAAAAAA&bo=qAJ0AQAAAAADAPo!&rf=viewer_4" };

		for (int i = 0; i < imageRes.length; i++) {
			RelativeLayout relativeLayout = (RelativeLayout) View.inflate(this,
					R.layout.loop_ad_item, null);

			ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.ad_image);
			imageView.setOnClickListener(new ClickAd(i));
			mImageLoader.DisplayImage(imageRes[i], imageView, false);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			listViews.add(relativeLayout);
		}
	}

    /**
     * set story list
     * @param page
     * @param action
     */
	public void setStory(final int page, final int action) {
        if(Network.checkNetWorkState(SubMainActivity.this)) {
			loadingDialog.startProgressDialog();
            this.page = page;
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String path = url + page;
                        JSONObject obj = HttpUtil.getRequestJson(path, null);
                        Integer err_code = obj.getInteger("errcode");
                        if (err_code == 0) {
                            JSONArray arry = obj.getJSONArray("data");
                            if (arry != null) {
                                ArrayList<News> array = News.getListFromJson(arry.toString());
                                if (action == 0) {
                                    newsList.clear();
                                    newsList.addAll(array);
                                } else {
                                    newsList.addAll(array);
                                }
                                handler.sendEmptyMessage(0);
                            } else {
                                handler.sendEmptyMessage(2);
                            }
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            /**
             * Network notification
             */
            Until.sendNetworkBroadcast(SubMainActivity.this);
        }
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0:
					if (adapter == null) {
						adapter = new StoryAdapter(self,newsList,R.layout.activity_story_cell);
						listview.setAdapter(adapter);
						listview.setOnItemClickListener(SubMainActivity.this);
					} else {
						adapter.notifyDataSetChanged();
					}

					ptrlv.onPullDownRefreshComplete();
					ptrlv.onPullUpRefreshComplete();
					ptrlv.setHasMoreData(hasMoreData);
					break;
				case 1:
					Toast.makeText(self, "没有更多！", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(self, "获取列表错误！", Toast.LENGTH_SHORT).show();
					break;
                default:
                    break;
			}
			loadingDialog.stopProgressDialog();
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
