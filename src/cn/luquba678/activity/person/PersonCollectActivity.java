package cn.luquba678.activity.person;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.adapter.CollectListAdapter;
import cn.luquba678.entity.CollectItem;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;
import cn.luquba678.view.PullToRefreshListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

public class PersonCollectActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener,
		OnRefreshListener<ListView> {
	private TextView top_text, collect_edit, collect_delete, title_top_cancle;
	private ImageView title_top_image;
	private ListView collect_listView;
	private CollectListAdapter adapter;
	private List<CollectItem> collectItem_list;
	private PullToRefreshListView ptrlv;
	private boolean isEditMode;
	private int page = 1;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
				collectItem_list = (List<CollectItem>) msg.obj;
				if(adapter==null){
					adapter = new CollectListAdapter(PersonCollectActivity.this, collectItem_list,R.layout.item_collect);
					collect_listView.setAdapter(adapter);
				}else {
					adapter.changeDate(collectItem_list);
				}
				if(msg.what == 2){
					adapter.deleteItem();
					changeTopState(false);
					adapter.showCheckBox(false);
				}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_collect);
		collectItem_list = new ArrayList<CollectItem>();
		initView();
		getCollectionData(page);
	}

	private void initView() {
		ptrlv = (PullToRefreshListView) findViewById(R.id.collect_listView);
		ptrlv.setPullLoadEnabled(true);
		ptrlv.setPullRefreshEnabled(false);
		ptrlv.setOnRefreshListener(this);
		collect_listView = ptrlv.getRefreshableView();
		collect_listView.setOnItemClickListener(this);
		collect_listView.setOnItemLongClickListener(this);
		top_text = (TextView) findViewById(R.id.top_text);
		top_text.setText("我的收藏");
		title_top_cancle = (TextView) findViewById(R.id.title_top_cancle);
		title_top_cancle.setOnClickListener(this);
		title_top_image = (ImageView) findViewById(R.id.title_top_image);
		title_top_image.setOnClickListener(this);
		collect_edit = (TextView) findViewById(R.id.collect_edit);
		collect_edit.setOnClickListener(this);
		collect_delete = (TextView) findViewById(R.id.collect_delete);
		collect_delete.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_top_image:
			finish();
			break;
		case R.id.collect_edit:
			changeTopState(true);
			adapter.showCheckBox(true);
			break;
		case R.id.collect_delete:
			deleteCollectionData();
			break;
		case R.id.title_top_cancle:
			changeTopState(false);
			adapter.showCheckBox(false);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		changeTopState(true);
		adapter.refershCheckBoxState(position);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (isEditMode) {
			adapter.refershCheckBoxState(position);
		} else {
			// startActivity(Por);
		}
	}

	private void changeTopState(boolean isDeleteItemState) {
		if (isDeleteItemState) {
			title_top_image.setVisibility(View.GONE);
			title_top_cancle.setVisibility(View.VISIBLE);
			collect_edit.setVisibility(View.GONE);
			collect_delete.setVisibility(View.VISIBLE);
			isEditMode = true;
		} else {
			title_top_image.setVisibility(View.VISIBLE);
			title_top_cancle.setVisibility(View.GONE);
			collect_edit.setVisibility(View.VISIBLE);
			collect_delete.setVisibility(View.GONE);
			isEditMode = false;
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	private void getCollectionData(final int page) {
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				String queryCollectionUrl = String.format(
						Const.QUERYCOLLECTION,
						User.getUID(PersonCollectActivity.this),
						User.getLoginToken(PersonCollectActivity.this), page
								+ "");

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
							arrayItem = JSONObject.parseObject(jsonArray
									.getString(i));
							conCollectItem = new CollectItem();
							conCollectItem.setCollect_title(arrayItem
									.getString("title"));
							conCollectItem.setCollect_imgUrl(arrayItem
									.getString("pic"));
							conCollectItem.setCollect_date(arrayItem
									.getString("createtime"));
							conCollectItem.setCollect_id(arrayItem
									.getString("id"));
							conCollectItem.setCollect_type(arrayItem
									.getString("type"));
							collectItem_list.add(conCollectItem);
						}
						Message msg = Message.obtain();
						msg.what = 1;
						msg.obj=collectItem_list;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
	}

	private void deleteCollectionData() {
		String deleteCollectionUrl = String.format(Const.DELETECOLLECTION,
				User.getUID(PersonCollectActivity.this),
				User.getLoginToken(PersonCollectActivity.this));
		MultipartEntity entity = new MultipartEntity();
		JSONObject obj;
		Gson gson = new Gson();
		String json = gson.toJson(collectItem_list);
		try {
			entity.addPart("list", new StringBody(json));
			obj = HttpUtil.getRequestJson(deleteCollectionUrl, entity);
			Message msg = handler.obtainMessage();
			msg.what = 2;
			msg.obj = collectItem_list;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
