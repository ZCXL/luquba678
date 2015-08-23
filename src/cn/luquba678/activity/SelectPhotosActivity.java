package cn.luquba678.activity;

import java.io.Serializable;
import java.util.List;

import cn.luquba678.R;
import cn.luquba678.photo.AlbumHelper;
import cn.luquba678.photo.ImageBucket;
import cn.luquba678.photo.ImageBucketAdapter;
import cn.luquba678.utils.SPUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectPhotosActivity extends Activity {
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);
		InitTopView();
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		initData();
		initView();
	}

	private void InitTopView() {
		TextView tv_topTextView = (TextView) findViewById(R.id.tv_top);
		tv_topTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SPUtils.put(SelectPhotosActivity.this, "head_img", "nopath");
				finish();
			}
		});
	}

	private void initData() {
		dataList = helper.getImagesBucketList(false);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(SelectPhotosActivity.this, dataList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SelectPhotosActivity.this,
						ImageGridActivity.class);
				intent.putExtra(SelectPhotosActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivity(intent);
				finish();
			}

		});
	}
}
