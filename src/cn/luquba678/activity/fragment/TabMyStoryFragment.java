package cn.luquba678.activity.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.BrowseImageDialog;
import cn.luquba678.activity.WdjyWriteActivity;
import cn.luquba678.activity.adapter.WdjyGridDataAdapter;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.GridItem;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;

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
	public static ProgressDialog dialog;
	private StickyGridHeadersGridView mGridView;
	private View writeBtn;

    private List<GridItem> mGridList = new ArrayList<GridItem>();
    public static WdjyWriteActivity activity;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

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
		findViewById(R.id.top_back).setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.top_text)).setText("寄语");

		writeBtn = getView().findViewById(R.id.id_tab_bottom_write);
		writeBtn.setOnClickListener(this);
		mGridView = (StickyGridHeadersGridView) getView().findViewById(R.id.asset_grid);

		mGridView.setOnItemClickListener(this);
		mGridView.setOnLongClickListener(this);

	}

    @Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		BrowseImageDialog dialogBrowseImage = new BrowseImageDialog(TabMyStoryFragment.this.getActivity(), mGridList, position);
		dialogBrowseImage.show();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (getAllWdjy()) {
			mGridView.setAdapter(new WdjyGridDataAdapter(getActivity(), mGridList, mGridView));
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

	}

	private boolean getAllWdjy() {
		mGridList.clear();
		try {
			String url = String.format(Const.QUERY_WORD, User.getUID(this.getActivity()),User.getLoginToken(getActivity()));
			JSONObject jsonObj = JSONObject.parseObject(HttpUtil.postRequestEntity(url, null));
			if (jsonObj.getInteger("errcode") == 0) {
				JSONArray jyItems = jsonObj.getJSONArray("data");
				mGridList = GridItem.getListFromJson(jyItems.toJSONString());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_tab_bottom_write:
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
