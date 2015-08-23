package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuchao.bean.Advertisement;
import com.zhuchao.http.Network;
import com.zhuchao.utils.ImageLoader;
import com.zhuchao.view_rewrite.LoadingDialog;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.StoryAdapter;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.FamousSays;
import cn.luquba678.entity.News;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.ImgScrollViewPager;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshListView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
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
    private RelativeLayout error_layout;
    private Button refresh_button;
	private ArrayList<Advertisement>advertisements=new ArrayList<Advertisement>();

	private LinearLayout back;
	private LoadingDialog loadingDialog;


    private ArrayList<News> newsList=new ArrayList<News>();
    private int page = 1;
    private boolean hasMoreData = false;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_main_page);
		initView();

	}

	private void initView(){
		loadingDialog=new LoadingDialog(SubMainActivity.this);
        /**
         * network error bg
         */
        error_layout=(RelativeLayout)findViewById(R.id.network_error);
        refresh_button=(Button)findViewById(R.id.network_error_button);
        refresh_button.setOnClickListener(this);

		back=(LinearLayout)findViewById(R.id.top_back);
		setOnClickLinstener(R.id.top_back);
		top_text = getView(R.id.top_text);
		String top_text_str = getIntent().getStringExtra("title");
		top_text.setText(top_text_str);
		mmPager = (ImgScrollViewPager) findViewById(R.id.pager_ad);
		View fa = getView(R.id.famous_says_text_area);

		if ("青春励志".equals(top_text_str)) {
			url = Const.STORY_QUERY;
			type = LZGS;
			getView(R.id.ad_relativeLayout).setVisibility(View.GONE);
			fa.setVisibility(View.VISIBLE);
			famous_says_author = getView(R.id.famous_says_author);
			famous_says = getView(R.id.famous_says_text);
		} else {
			type = ZYXD;
			getView(R.id.ad_relativeLayout).setVisibility(View.VISIBLE);
			fa.setVisibility(View.GONE);
			url = Const.CHAMPION_EXPERIENCE;
		}

		ptrlv = getView(R.id.listview_stories);
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(true);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		listview = ptrlv.getRefreshableView();
        ptrlv.setOnRefreshListener(this);
        /**
         * load data
         */
        loadData();
	}
    /**
     * load data from server
     */
    private void loadData(){
        if(Network.checkNetWorkState(this)) {
            if(error_layout.getVisibility()==View.VISIBLE)
                error_layout.setVisibility(View.INVISIBLE);
            try {
                JSONObject obj = HttpUtil.getRequestJson(Const.QUERY_FAMOUS_SAYS, null);
                Integer errcode = obj.getInteger("errcode");
                if (errcode == 0) {
                    JSONObject array = obj.getJSONObject("data");
                    FamousSays famousSays = FamousSays.getListFromJson(array.toString());

                    famous_says.setText("    " + famousSays.getContent());
                    famous_says_author.setText("——" + famousSays.getAuthor());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /**
             * advertisement
             */
            initAds();
            /**
             * set story
             */
            setStory(page, 0);
        }else{
			Toast.makeText(SubMainActivity.this,"未连接网络",Toast.LENGTH_SHORT).show();
            if(error_layout.getVisibility()==View.INVISIBLE)
                error_layout.setVisibility(View.VISIBLE);
        }
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
    /**
     * init view pager
     */
	private void InitViewPager() {
		mImageLoader = new ImageLoader(this);
		listViews=new ArrayList<View>();
        try {
            JSONObject obj = HttpUtil.getRequestJson(Const.QUERY_ADS_URL, null);
            int err_code = obj.getIntValue("errcode");
            if (err_code == 0) {
                JSONArray array = obj.getJSONArray("data");
                for (int i = 0, j = array.size(); i < j; i++) {
                    Advertisement advertisement = new Advertisement(array.get(i).toString());
                    advertisements.add(advertisement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < advertisements.size(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) View.inflate(this, R.layout.loop_ad_item, null);

            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.ad_image);
            imageView.setTag(advertisements.get(i).getPic());
            mImageLoader.DisplayImage(advertisements.get(i).getPic(), imageView);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            relativeLayout.setOnClickListener(new ClickAd(i));
            relativeLayout.setTag(advertisements.get(i));
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
            case R.id.network_error_button:
                loadData();
                break;
		}
	}

	class ClickAd implements OnClickListener {

		int id;

		public ClickAd(int id) {
			this.id = id;
		}

		public void onClick(View v) {// 设置图片点击事件
			Advertisement advertisement=(Advertisement)v.getTag();
			Uri uri = Uri.parse(advertisement.getUrl());
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
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
