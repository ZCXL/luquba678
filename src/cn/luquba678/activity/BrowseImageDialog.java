package cn.luquba678.activity;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Gallery;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.adapter.ImageAdapter;
import cn.luquba678.entity.GridItem;
import cn.luquba678.ui.FullScreenDialog;

public class BrowseImageDialog extends FullScreenDialog implements android.view.View.OnClickListener {
	private Gallery mGallery;
	private Context context;
	private List<GridItem> mGridList;
	private int mPos = 0;
	private TextView top_text;

	public BrowseImageDialog(Activity context, List<GridItem> mGridList, int position) {
		super(context);
		// initTitle(findViewById(R.id.browse_image_top),"寄语浏览");
		this.context = context;
		this.mGridList = mGridList;
		mPos = position;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wdjy_gallery_imgs);
		initView();

	}

	private void initView() {
		mGallery = (Gallery) findViewById(R.id.gallery_wdjy_imgs);
		mGallery.setAdapter(new ImageAdapter(context, mGridList, R.layout.image_view,this));
		mGallery.setSelection(mPos);

	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

}
