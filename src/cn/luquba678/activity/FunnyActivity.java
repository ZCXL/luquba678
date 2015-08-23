package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuchao.http.Network;
import com.zhuchao.view_rewrite.LoadingDialog;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.StoryAdapter;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.Until;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshListView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FunnyActivity extends CommonActivity implements OnItemClickListener, OnRefreshListener<ListView> {
	private RadioButton rb_funny_school_btn, rb_funny_image_btn, rb_jokes_btn;
	private ListView listview;
	private StoryAdapter adapter;
	public static final int XYQS=1;
	public static final int GXDZ=2;
	public static final int NHT=3;
	int currentType = XYQS;

	//data from three block;
	private ArrayList<News>stories,jokes,pictures;
	//current page list;
	private ArrayList<News> newsList;
	//page of every block;
	private HashMap<Integer,Integer>map;

	private PullToRefreshListView ptrlv;

	private LoadingDialog loadingDialog;

	private RelativeLayout error_layout;
	private Button refresh_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_funny);
		initView();

	}

	private void initView() {
		loadingDialog=new LoadingDialog(FunnyActivity.this);
		stories=new ArrayList<News>();
		jokes=new ArrayList<News>();
		pictures=new ArrayList<News>();
		newsList=new ArrayList<News>();

		map=new HashMap<Integer, Integer>();
		map.put(GXDZ,1);
		map.put(NHT,1);
		map.put(XYQS,1);

		/**
		 * network error bg
		 */
		error_layout=(RelativeLayout)findViewById(R.id.network_error);
		refresh_button=(Button)findViewById(R.id.network_error_button);
		refresh_button.setOnClickListener(this);

		setOnClickLinstener(R.id.top_back, R.id.funny_school_btn, R.id.funny_image_btn, R.id.jokes_btn, R.id.funny_school, R.id.funny_image, R.id.jokes);
		rb_funny_school_btn = getView(R.id.funny_school);
		rb_funny_image_btn = getView(R.id.funny_image);
		rb_jokes_btn = getView(R.id.jokes);
		((TextView) findViewById(R.id.top_text)).setText("轻松一刻");

		ptrlv = getView(R.id.funnylistview);
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(true);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		listview = ptrlv.getRefreshableView();
		ptrlv.setOnRefreshListener(this);

		adapter = new StoryAdapter(self,newsList, R.layout.activity_funny_cell);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(FunnyActivity.this);

		setFunny(map.get(XYQS),XYQS, 0);
	}
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        setFunny(1, currentType, 1);
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
                currentType = XYQS;
                changePage(currentType);
                break;
            case R.id.funny_image:
                currentType = NHT;
                changePage(currentType);
                break;
            case R.id.jokes:
                currentType = GXDZ;
                changePage(currentType);
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
			case R.id.network_error_button:
				setFunny(map.get(currentType), currentType, ADD);
				break;
            default:
                break;
        }
    }


    private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0:
					if(currentType==XYQS)
						adapter.changeDateInThread(stories);
					else if(currentType==NHT)
						adapter.changeDateInThread(pictures);
					else if(currentType==GXDZ)
						adapter.changeDateInThread(jokes);
					break;
				case 1:
					Toast.makeText(self, "没有更多！", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(self, "获取列表错误！", Toast.LENGTH_SHORT).show();
					break;
			}
            /**
             * finish load
             */
			ptrlv.onPullDownRefreshComplete();
			ptrlv.onPullUpRefreshComplete();
			ptrlv.setHasMoreData(true);
            loadingDialog.stopProgressDialog();
			super.handleMessage(msg);
		}
	};

	public void setFunny(final int page, final int type, final int action) {
		if(Network.checkNetWorkState(FunnyActivity.this)) {
			if(error_layout.getVisibility()==View.VISIBLE)
				error_layout.setVisibility(View.INVISIBLE);
            loadingDialog.startProgressDialog();
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					try {
						/**
						 * get data from server
						 */
						String url = String.format(Const.FUNNY_QUERY, page, type);
						JSONObject obj = HttpUtil.getRequestJson(url, null);
						Integer errcode = obj.getInteger("errcode");

						/**
						 * add data
						 */
						if (errcode == 0) {
							JSONArray array = obj.getJSONArray("data");
							ArrayList<News> arrays = News.getListFromJson(array.toString());
							if (arrays != null && arrays.size() > 0) {
								/**
								 * sava data
								 */
								processData(arrays, action, type);
								handler.sendEmptyMessage(0);
							} else {
								handler.sendEmptyMessage(1);
							}
						} else {
							handler.sendEmptyMessage(2);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}else{
			/**
			 * send network broadcast
			 */
			Toast.makeText(FunnyActivity.this,"未连接网络",Toast.LENGTH_SHORT).show();
			if(error_layout.getVisibility()==View.INVISIBLE)
				error_layout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		News news;
        if(currentType==XYQS)
            news=stories.get(arg2);
        else if(currentType==NHT)
            news=pictures.get(arg2);
        else{
            news=jokes.get(arg2);
        }
		//FunnyDetailActivity.intentToDetailNews(news, self, currentType);
		CommonNewsActivity.intentToDetailNews(news,self,currentType);
	}

	public void addFunny() {
		setFunny(map.get(currentType),currentType,0);
	}

	/**
	 * change page value
	 * only is there no data,we would get data
	 * @param currentType
	 */
	private void changePage(int currentType){
		if(currentType==XYQS)
			if(stories.size()>0)
				adapter.changeDateInThread(stories);
			else{
				setFunny(map.get(XYQS),XYQS, 0);
			}
		else if(currentType==NHT)
			if(pictures.size()>0){
				adapter.changeDateInThread(pictures);
			}else{
				setFunny(map.get(NHT),NHT, 0);
			}
		else if(currentType==GXDZ)
			if(jokes.size()>0){
				adapter.changeDateInThread(jokes);
			}else{
				setFunny(map.get(GXDZ),GXDZ, 0);
			}
	}

	/**
	 * add or reset value in array list
	 * @param arrays
	 * @param action
	 * @param type
	 */
	private void processData(ArrayList<News>arrays,int action,int type){
		/**
		 * update data of page in different block
		 */
		int page=map.get(type);
		if(action==0){
			page++;
			map.put(type,page);
		}else{
			map.put(type,1);
		}
		/**
		 * change type of data
		 */
		switch (type){
			case XYQS:
				if(action==0){
					stories.addAll(arrays);
				}else{
					stories.clear();
					stories.addAll(arrays);
				}
				break;
			case GXDZ:
				if(action==0){
					jokes.addAll(arrays);
				}else{
					jokes.clear();
					jokes.addAll(arrays);
				}
				break;
			case NHT:
				if(action==0){
					pictures.addAll(arrays);
				}else{
					pictures.clear();
					pictures.addAll(arrays);
				}
				break;
		}
	}
}
