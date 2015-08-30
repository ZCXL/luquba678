package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.HashMap;
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
import cn.luquba678.entity.News;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshGridView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PrettySchoolMateActivity extends CommonActivity implements OnClickListener, OnRefreshListener<GridView>, OnItemClickListener {

	private GridView mGrid;
	private CommonAdapter<News> adapter;
	private ImageLoader imageLoader;
	private RadioButton rb_pretty_girl, rb_pretty_boy;
	final static int BOY = 2, GIRL = 1;

    private static final int PRETTY_MATE = 3;
    private static final int ACTION_ADD=0,ACTION_REFRESH=1;
    private ArrayList<News> girls,boys;
    private HashMap<Integer,Integer>map;
    private int currentType;
	private PullToRefreshGridView ptrlv;
	public static final String PRETTY = "校花校草";
    private LoadingDialog loadingDialog;

    private RelativeLayout error_layout;
    private Button refresh_button;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.top_back:
                this.finish();
                break;
            case R.id.pretty_boy:
                currentType=BOY;
                changePage(BOY);
                break;
            case R.id.pretty_girl:
                currentType=GIRL;
                changePage(GIRL);
                break;
            case R.id.pretty_girl_btn:
                rb_pretty_girl.performClick();
                break;
            case R.id.pretty_boy_btn:
                rb_pretty_boy.performClick();
                break;
            case R.id.get_more:
                setPretty(map.get(currentType), currentType, ACTION_ADD);
                break;
            case R.id.network_error_button:
                setPretty(map.get(currentType), currentType, ACTION_ADD);
                break;
            default:
                break;
		}

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pretty_school_mate);
        /**
         * network error bg
         */
        error_layout=(RelativeLayout)findViewById(R.id.network_error);
        refresh_button=(Button)findViewById(R.id.network_error_button);
        refresh_button.setOnClickListener(this);

		setOnClickLinstener(R.id.top_back, R.id.pretty_boy, R.id.pretty_girl, R.id.pretty_girl_btn, R.id.pretty_boy_btn);
		((TextView) findViewById(R.id.top_text)).setText(PRETTY);
		imageLoader = new ImageLoader(this);
		rb_pretty_girl = getView(R.id.pretty_girl);
		rb_pretty_boy = getView(R.id.pretty_boy);

        loadingDialog=new LoadingDialog(this);
		ptrlv = getView(R.id.pretty_school_mates);
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(true);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		mGrid = ptrlv.getRefreshableView();
        mGrid.setNumColumns(2);
		ptrlv.setOnRefreshListener(this);

        /**
         * init base data
         */
        girls=new ArrayList<News>();
        boys=new ArrayList<News>();
        map=new HashMap<Integer, Integer>();
        map.put(GIRL, 1);
        map.put(BOY,1);
        currentType=GIRL;
        setAdapter();

        /**
         * load new data
         */
		setPretty(map.get(GIRL), GIRL, ACTION_ADD);
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		setPretty(1,currentType, ACTION_REFRESH);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
        setPretty(map.get(currentType),currentType, ACTION_ADD);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		News pretty=null;
        if(currentType==GIRL)
            pretty=girls.get(arg2);
        else if(currentType==BOY)
            pretty=boys.get(arg2);
		CommonNewsActivity.intentToDetailNews(pretty, self, PRETTY_MATE);
	}

	public void setPretty(final int page, final int sex, final int action) {
		if(Network.checkNetWorkState(PrettySchoolMateActivity.this)) {
            if(error_layout.getVisibility()==View.VISIBLE)
                error_layout.setVisibility(View.INVISIBLE);
            loadingDialog.startProgressDialog();
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					try {
						String url = String.format(Const.PRETTY_QUERY, page, sex);
						JSONObject obj = HttpUtil.getRequestJson(url, null);
						Integer errcode = obj.getInteger("errcode");
						if (errcode == 0) {
							JSONArray array = obj.getJSONArray("data");
							ArrayList<News> arrays= News.getListFromJson(array.toString());
							if (arrays != null && arrays.size() > 0) {
								processData(arrays,action,sex);
                                handler.sendEmptyMessage(1);
							}else{
                                handler.sendEmptyMessage(3);
                            }
						}else{
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
            Toast.makeText(PrettySchoolMateActivity.this,"未连接网络",Toast.LENGTH_SHORT).show();
			if(error_layout.getVisibility()==View.INVISIBLE)
                error_layout.setVisibility(View.VISIBLE);
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                   if(currentType==GIRL){
                       //adapter.notifyDataSetChanged();
                       adapter.changeDateInThread(girls);
                   }else if(currentType==BOY){
                       //adapter.notifyDataSetChanged();
                       adapter.changeDateInThread(boys);
                   }
                    break;
                case 2:
                    Toast.makeText(self, "没有更多！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(self, "获取列表错误！", Toast.LENGTH_SHORT).show();
                    break;
            }
            ptrlv.onPullDownRefreshComplete();
            ptrlv.onPullUpRefreshComplete();
            ptrlv.setHasMoreData(true);
            loadingDialog.stopProgressDialog();
            super.handleMessage(msg);
		}
	};

    /**
     * set adapter
     */
    private void setAdapter(){
        adapter = new CommonAdapter<News>(PrettySchoolMateActivity.this, girls, R.layout.activity_pretty_school_mate_item) {

            @Override
            public void setViews(ViewHolder holder, News t, int position) {
                ImageView imageView = holder.getView(R.id.headImage);
                TextView name = holder.getView(R.id.name);
                String picUrl = t.getPic();
                imageView.setImageBitmap(null);
                imageLoader.DisplayImage(picUrl, imageView);
                name.setText(t.getTitle());
            }
        };
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(PrettySchoolMateActivity.this);
        mGrid.setSelector(R.drawable.list_item_bg);
    }
    /**
     * change page value
     * only is there no data,we would get data
     * @param currentType
     */
    private void changePage(int currentType){
        if(currentType==GIRL)
            if(girls.size()>0)
                adapter.changeDateInThread(girls);
            else {
                setPretty(map.get(GIRL),GIRL, ACTION_ADD);
            }
        else if(currentType==BOY)
            if(boys.size()>0){
                adapter.changeDateInThread(boys);
            }else{
                setPretty(map.get(BOY),BOY, ACTION_ADD);
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
        if(action==ACTION_ADD){
            page++;
            map.put(type,page);
        }else{
            map.put(type,1);
        }
        /**
         * change type of data
         */
        switch (type){
            case GIRL:
                if(action==ACTION_ADD){
                    girls.addAll(arrays);
                }else{
                    girls.clear();
                    girls.addAll(arrays);
                }
                break;
            case BOY:
                if(action==0){
                    boys.addAll(arrays);
                }else{
                    boys.clear();
                    boys.addAll(arrays);
                }
                break;
        }
    }
}
