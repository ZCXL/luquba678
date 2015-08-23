package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import cn.luquba678.R;
import cn.luquba678.photo.AlbumHelper;
import cn.luquba678.photo.Bimp;
import cn.luquba678.photo.ImageGridAdapter;
import cn.luquba678.photo.ImageGridAdapter.TextCallback;
import cn.luquba678.photo.ImageItem;
import cn.luquba678.utils.SPUtils;

public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	Button bt;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择1张图片", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_grid);
		InitTopView();
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);
		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				if (Bimp.act_bool) {
					Bimp.act_bool = false;
					finish();
				}
				Bimp.drr.clear();
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < 2) {
						Bimp.drr.add(list.get(i));
					}
				}
				if (list.size() > 0) {
					SPUtils.put(ImageGridActivity.this, "head_img", list.get(0));
				}
				finish();
			}

		});
	}

	private void InitTopView() {
		TextView tv_topTextView = (TextView) findViewById(R.id.tv_top);
		tv_topTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SPUtils.put(ImageGridActivity.this, "head_img", "nopath");
				finish();
			}
		});
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

	}
}
