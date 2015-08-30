package cn.luquba678.activity.person;

import cn.luquba678.activity.CommonNewsActivity;
import cn.luquba678.view.PullToRefreshBase;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.CollectListAdapter;
import cn.luquba678.entity.CollectItem;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhuchao.http.Network;
import com.zhuchao.view_rewrite.LoadingDialog;
import com.zhuchao.view_rewrite.PullToRefreshListView;
import com.zhuchao.view_rewrite.SwipeListView;

public class PersonCollectActivity extends Activity implements OnClickListener, OnItemClickListener,CollectListAdapter.OnDeleteListener, PullToRefreshBase.OnRefreshListener<ListView> {
	private TextView collect_edit, collect_delete, title_top_cancel;
	private ImageView title_top_image;
	private LinearLayout back;
	private SwipeListView collect_listView;
	private CollectListAdapter adapter;
	private ArrayList<CollectItem> collectItem_list;
	private PullToRefreshListView ptrlv;
	private boolean isEditMode;
	private int page = 1;
	private LoadingDialog loadingDialog;
	private RelativeLayout delete_ly,edit_ly;
    private ArrayList<String>result;

	private RelativeLayout error_layout;
	private Button refresh_button;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
					/**
					 * update list
					 */
                    adapter.changeDate(collectItem_list);
                    break;
                case 2:
                    /**
                     * remove items that have been delete.
                     */
                    for(int i=0,j=result.size();i<j;i++){
                        for(int k=0;k<collectItem_list.size();k++)
                            if(result.get(i).equals(collectItem_list.get(k).getId())) {
                                collectItem_list.remove(k);
                                break;
                            }
                    }
					collect_listView.hiddenRight(collect_listView.mCurrentItemView);
					/**
					 * hide check box
					 */
                    changeTopState(false);
                    /**
                     * update list
                     */
                    adapter.showCheckBox(false);
                    break;
            }
			loadingDialog.stopProgressDialog();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_collect);
		/**
		 * init view
		 */
		initView();
		/**
		 * get collection
		 */
		getCollectionData(page);
	}

	private void initView() {
		/**
		 * network error bg
		 */
		error_layout=(RelativeLayout)findViewById(R.id.network_error);
		refresh_button=(Button)findViewById(R.id.network_error_button);
		refresh_button.setOnClickListener(this);
		/**
		 * find view
		 */
		loadingDialog=new LoadingDialog(this);
		ptrlv = (PullToRefreshListView) findViewById(R.id.collect_listView);
		ptrlv.setPullLoadEnabled(true);
		ptrlv.setPullRefreshEnabled(false);
		ptrlv.setOnRefreshListener(this);
		collect_listView =(SwipeListView)ptrlv.getRefreshableView();
		collect_listView.setOnItemClickListener(this);
		title_top_cancel = (TextView) findViewById(R.id.title_top_cancle);
		title_top_cancel.setOnClickListener(this);
		title_top_image = (ImageView) findViewById(R.id.title_top_image);
		title_top_image.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.top_back);
		back.setOnClickListener(this);
		collect_edit = (TextView) findViewById(R.id.collect_edit);
		collect_edit.setOnClickListener(this);
		collect_delete = (TextView) findViewById(R.id.collect_delete);
		collect_delete.setOnClickListener(this);
		edit_ly=(RelativeLayout)findViewById(R.id.collect_edit_ly);
		delete_ly=(RelativeLayout)findViewById(R.id.collect_delete_ly);
		edit_ly.setOnClickListener(this);
		delete_ly.setOnClickListener(this);
        /**
         * init list view
         */
        collectItem_list = new ArrayList<CollectItem>();
        result=new ArrayList<String>();
        adapter = new CollectListAdapter(PersonCollectActivity.this, collectItem_list,R.layout.item_collect);
		/**
		 * set delete interface
		 */
        adapter.setOnDeleteListener(this);
        collect_listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.network_error_button:
				/**
				 * get collection
				 */
				getCollectionData(page);
				break;
			case R.id.title_top_image:
				finish();
				break;
			case R.id.top_back:
				if(isEditMode){
					Log.d("zhuchao","cancel");
					changeTopState(false);
					adapter.showCheckBox(false);
				}else {
					finish();
				}
				break;
			case R.id.collect_edit:
				changeTopState(true);
				adapter.showCheckBox(true);
				break;
			case R.id.collect_delete:
				/**
				 * delete,and system will roll back
				 */
				adapter.deleteItem();
				break;
			case R.id.title_top_cancle:
				Log.d("zhuchao","cancel");
				changeTopState(false);
				adapter.showCheckBox(false);
				break;
			case R.id.collect_edit_ly:
				changeTopState(true);
				adapter.showCheckBox(true);
				break;
			case R.id.collect_delete_ly:
				/**
				 * delete,and system will roll back
				 */
				adapter.deleteItem();
				break;
			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (isEditMode) {
			adapter.refreshCheckBoxState(position);
		} else {
			CommonNewsActivity.intentToDetailNews(collectItem_list.get(position),PersonCollectActivity.this,1);
		}
	}

	/**
	 * change top state
	 * @param isDeleteItemState
	 */
	private void changeTopState(boolean isDeleteItemState) {
		if (isDeleteItemState) {
			title_top_image.setVisibility(View.GONE);
			title_top_cancel.setVisibility(View.VISIBLE);
			edit_ly.setVisibility(View.GONE);
			delete_ly.setVisibility(View.VISIBLE);
			isEditMode = true;
		} else {
			title_top_image.setVisibility(View.VISIBLE);
			title_top_cancel.setVisibility(View.GONE);
			edit_ly.setVisibility(View.VISIBLE);
			delete_ly.setVisibility(View.GONE);
			isEditMode = false;
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        /**
         * get more
         */
		getCollectionData(page);
	}

	/**
	 * get collection set in some page
	 * @param page
	 */
	private void getCollectionData(final int page) {
		if(Network.checkNetWorkState(this)) {
			loadingDialog.startProgressDialog();
			if(error_layout.getVisibility()==View.VISIBLE)
				error_layout.setVisibility(View.INVISIBLE);
			Executors.newSingleThreadExecutor().execute(new Runnable() {

				@Override
				public void run() {
					String queryCollectionUrl = String.format(Const.QUERYCOLLECTION, User.getUID(PersonCollectActivity.this), User.getLoginToken(PersonCollectActivity.this), page + "");

					MultipartEntity entity = new MultipartEntity();
					try {
						JSONObject obj;
						obj = HttpUtil.getRequestJson(queryCollectionUrl, entity);
						Integer errcode = obj.getInteger("errcode");
						JSONObject arrayItem;
						CollectItem conCollectItem;
						if (errcode == 0) {
							JSONArray jsonArray = (JSONArray) obj.get("data");
							for (int i = 0; i < jsonArray.size(); i++) {
								arrayItem = JSONObject.parseObject(jsonArray.getString(i));
								conCollectItem = new CollectItem(arrayItem.toJSONString());
								collectItem_list.add(conCollectItem);
							}
							PersonCollectActivity.this.page++;
							handler.sendEmptyMessage(1);
						} else {
							handler.sendEmptyMessage(3);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			});
		}else{
			Toast.makeText(PersonCollectActivity.this, "未连接网络", Toast.LENGTH_SHORT).show();
			if(error_layout.getVisibility()==View.INVISIBLE)
				error_layout.setVisibility(View.VISIBLE);
		}
	}

    @Override
    public void onDelete(final ArrayList<CollectItem> list) {
		if(Network.checkNetWorkState(this)) {
			/**
			 * format address url
			 */
			String deleteCollectionUrl = String.format(Const.DELETECOLLECTION, User.getUID(PersonCollectActivity.this), User.getLoginToken(PersonCollectActivity.this));

			/**
			 *set parameters
			 */
			MultipartEntity entity = new MultipartEntity();
			Gson gson = new Gson();
			/**
			 * get all id
			 */
			ArrayList<String> ids = new ArrayList<String>();

			for (int i = 0; i < list.size(); i++) {
				ids.add(list.get(i).getId());
			}
			String json = gson.toJson(ids);
			try {
				entity.addPart("list", new StringBody(json));

				Log.d("zhuchao", json);
				JSONObject obj = HttpUtil.getRequestJson(deleteCollectionUrl, entity);
				/**
				 * process data to update
				 */
				JSONArray array = obj.getJSONArray("data");
				result.clear();
				for (int i = 0, j = array.size(); i < j; i++)
					result.add(array.getString(i));

				/**
				 * start upload list view
				 */
				handler.sendEmptyMessage(2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Toast.makeText(PersonCollectActivity.this, "未连接网络", Toast.LENGTH_SHORT).show();
		}
    }
}
