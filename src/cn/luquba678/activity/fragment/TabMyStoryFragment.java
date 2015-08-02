package cn.luquba678.activity.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.BrowseImageDialog;
import cn.luquba678.activity.CommonActivity;
import cn.luquba678.activity.WdjyWriteActivity;
import cn.luquba678.activity.adapter.WdjyGridDataAdapter;
import cn.luquba678.activity.listener.BackChangeOnTouchListener;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.GridItem;
import cn.luquba678.entity.User;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.ui.YMComparator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

/**
 * @ClassName: TabCFm
 * @author Panyy
 * @date 2013 2013年11月6日 下午4:06:47
 *
 */
public class TabMyStoryFragment extends Fragment implements OnItemClickListener,OnClickListener,OnLongClickListener{
	BackChangeOnTouchListener back;
	int keleiChoosed;
	public static ProgressDialog dialog;
	private StickyGridHeadersGridView mGridView;
	private View writeBtn;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.wdjy_main_activity, container, false);
	}

	public View findViewById(int id) {
		return this.getView().findViewById(id);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findViewById(R.id.top_back).setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.top_text)).setText("寄语");

		writeBtn = getView().findViewById(R.id.id_tab_bottom_write);
		writeBtn.setOnClickListener(this);
		mGridView = (StickyGridHeadersGridView) getView().findViewById(
				R.id.asset_grid);

		mGridView.setOnItemClickListener(this);
		mGridView.setOnLongClickListener(this);

	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// Intent intent = new Intent();
		// intent.setClass(getActivity(), WdjyBrowseImgActivity.class);
		// intent.putExtra("ImgIdx", position);
		// intent.putExtra("ImgList", (Serializable) mGridList);
		// startActivity(intent);
		BrowseImageDialog dialogBrowseImage = new BrowseImageDialog(
				TabMyStoryFragment.this.getActivity(), mGridList, position);
		dialogBrowseImage.show();
	}

	private List<GridItem> mGridList = new ArrayList<GridItem>();

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (getAllWdjy()) {
			mGridView.setAdapter(new WdjyGridDataAdapter(getActivity(),
					mGridList, mGridView));
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * 根据条件加载博客
	 */
	public void flushBlogs(int i) {
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	// 定义发送请求的方法
	private JSONObject queryAll(String uid) throws Exception {
		// 定义发送请求的URL
		// String url = "http://120.26.112.250/api/sendword/queryAll";
		String url = String.format(Const.QUERY_WORD,
				User.getUID(this.getActivity()),
				User.getLoginToken(getActivity()));
		// 发送请求
		return JSONObject.parseObject(HttpUtil.postRequestEntity(url, null));
	}

	private boolean getAllWdjy() {
		mGridList.clear();
		try {
			String url = String.format(Const.QUERY_WORD,
					User.getUID(this.getActivity()),
					User.getLoginToken(getActivity()));
			JSONObject jsonObj = JSONObject.parseObject(HttpUtil
					.postRequestEntity(url, null));
			if (jsonObj.getInteger("errcode") == 0) {
				JSONArray jyItems = jsonObj.getJSONArray("data");
				mGridList = GridItem.getListFromJson(jyItems.toJSONString());
				/*for (int i = 0; i < jyItems.size(); ++i) {
					JSONObject oj = jyItems.getJSONObject(i);
					GridItem mGridItem = new GridItem(oj.getString("pic"),
							oj.getLong("createtime"), oj.getString("content"));
					mGridList.add(mGridItem);
				}*/
				// System.out.println("------------>大小        "+mGridList.size());
				//Collections.sort(mGridList, new YMComparator());

				/*
				 * for (ListIterator<GridItem> it = mGridList.listIterator(); it
				 * .hasNext();) { GridItem mGridItem = it.next(); String ym[] =
				 * mGridItem.getTime().split("-"); if
				 * (!sectionMap.containsKey(ym[0])) {
				 * mGridItem.setSection(section); sectionMap.put(ym[0],
				 * section); section++; } else {
				 * mGridItem.setSection(sectionMap.get(ym[0])); } }
				 */

				return true;
			}
		} catch (Exception e) {
			// DialogUtil.showDialog(getActivity(), "服务器响应异常，请稍后再试！", false);
			e.printStackTrace();
		}

		return false;
	}
public static WdjyWriteActivity activity;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_tab_bottom_write:
			/*Intent intent = new Intent(getActivity(),
					WdjyWriteActivity.class);
			startActivity(intent);*/
			activity=new WdjyWriteActivity(getActivity());
			activity.show();
			break;

		default:
			break;
		}
		
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}

}
