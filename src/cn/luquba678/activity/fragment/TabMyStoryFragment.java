package cn.luquba678.activity.fragment;

import java.nio.charset.Charset;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.BrowseImageDialog;
import cn.luquba678.activity.WdjyWriteActivity;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshListView;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuchao.adapter.WishAdapter;
import com.zhuchao.bean.Wish;

/**
 * @ClassName: TabCFm
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:47
 *
 */
public class TabMyStoryFragment extends Fragment implements OnClickListener,PullToRefreshBase.OnRefreshListener<ListView>,AdapterView.OnItemClickListener,BrowseImageDialog.OnChangeListener {
	private ImageView writeBtn;

	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private WishAdapter adapter;
	private LinearLayout write_bg;
	private ArrayList<Wish>wishs;

	private int page=1;
	public final static int ADD=0,CHANGE=1;
    public static WdjyWriteActivity activity;

    private boolean isInit=false;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					adapter.notifyDataSetChanged();
					break;
				case 1:
					break;
				case 2:
					break;
			}
            pullToRefreshListView.onPullDownRefreshComplete();
            pullToRefreshListView.onPullUpRefreshComplete();
            pullToRefreshListView.setHasMoreData(true);
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.wdjy_main_activity, container, false);
	}

	public View findViewById(int id) {
		return this.getView().findViewById(id);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((TextView) findViewById(R.id.top_text)).setText("寄语");

		writeBtn = (ImageView)getView().findViewById(R.id.btn_tab_bottom_write);
		writeBtn.setOnClickListener(this);
		write_bg=(LinearLayout)findViewById(R.id.write_bg);
		write_bg.setOnClickListener(this);

		pullToRefreshListView=(PullToRefreshListView)getView().findViewById(R.id.wish_list);
		// 设置下拉刷新可用
		pullToRefreshListView.setPullRefreshEnabled(true);
		// 设置上拉加载可用
		pullToRefreshListView.setPullLoadEnabled(true);
        pullToRefreshListView.setOnRefreshListener(this);
		listView=pullToRefreshListView.getRefreshableView();
		wishs=new ArrayList<Wish>();

		adapter=new WishAdapter(wishs,getActivity());
        listView.setSelector(R.drawable.list_item_bg);
        listView.setOnItemClickListener(this);
		listView.setAdapter(adapter);
	}


	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
        if(!isInit) {
            getWish(page, ADD);
            isInit=true;
        }
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==2){
			if(data!=null){
				Bundle bundle=data.getExtras();
				if(bundle!=null) {
					Wish wish = bundle.getParcelable("wish");
					wishs.add(0,wish);
					adapter.notifyDataSetChanged();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getWish(int page,int action) {
		try {
			String url = String.format(Const.QUERY_WORD, User.getUID(this.getActivity()),User.getLoginToken(getActivity()));
			url +="&page="+String.valueOf(page);
			JSONObject jsonObj =HttpUtil.getRequestJson(url, null);
			if (jsonObj.getInteger("errcode") == 0) {
				JSONArray jyItems = jsonObj.getJSONArray("data");
				ArrayList<Wish> temp=new ArrayList<Wish>();
				for(int i=0;i<jyItems.size();i++){
					Wish wish=new Wish(jyItems.get(i).toString());
					temp.add(wish);
				}
				if(action==ADD)
					wishs.addAll(temp);
				else if(action==CHANGE) {
					wishs.clear();
					wishs.addAll(temp);
				}
				handler.sendEmptyMessage(0);
			}else{
				handler.sendEmptyMessage(1);
			}
		} catch (Exception e) {
			handler.sendEmptyMessage(2);
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_tab_bottom_write:
				activity=new WdjyWriteActivity(getActivity());
				activity.show();
				break;
			case R.id.write_bg:
				activity=new WdjyWriteActivity(getActivity());
				activity.show();
				break;
			default:
				break;
		}
		
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		getWish(page=1,CHANGE);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        page++;
		getWish(page,ADD);
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(position==0){
			activity=new WdjyWriteActivity(getActivity());
			activity.show();
		}else {
			BrowseImageDialog dialogBrowseImage = new BrowseImageDialog(TabMyStoryFragment.this.getActivity(), wishs, position-1);
			dialogBrowseImage.setOnChangeListener(this);
			dialogBrowseImage.show();
		}
    }

	@Override
	public void onChange(ArrayList<Wish> wishs) {
		this.wishs=wishs;
		adapter.notifyDataSetChanged();
	}
}
