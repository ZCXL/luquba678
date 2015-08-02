package cn.luquba678.activity.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import cn.luquba678.R;
import cn.luquba678.activity.BrowseImageDialog;
import cn.luquba678.entity.GridItem;
import cn.luquba678.utils.FileCache;
import cn.luquba678.utils.ImageLoader;

public class ImageAdapter extends CommonAdapter<GridItem> implements OnClickListener  {
	ImageLoader imageLoader;
	BrowseImageDialog browseImageDialog;
	public ImageAdapter(Context context, List<GridItem> dates, int layoutId,BrowseImageDialog browseImageDialog) {
		super(context, dates, layoutId);
		imageLoader = new ImageLoader(context);
		this.browseImageDialog= browseImageDialog;
		// TODO Auto-generated constructor stub
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public void setViews(ViewHolder holder, GridItem t, int position) {
		ImageView i = holder.getView(R.id.image_view);
		Bitmap bm = null;
		// String
		// path=FileManager.getSaveFilePath()+FileCache.hashKeyForDisk(mGridList.get(position).getPath())+"jpg";
		String path = t.getPath();
		File f = FileCache.getFile(path);
		imageLoader.DisplayImage(path, i, false);
		TextView top = holder.getView(R.id.top_text);
		holder.getView(R.id.top_back).setOnClickListener(this);
		top.setText(t.getTime("yyyy年MM月dd日"));
		if (f.exists()) {
			bm = BitmapFactory.decodeFile(f.getAbsolutePath());
		}
		if (bm == null) {
			i.setImageResource(R.drawable.empty_photo);
		} else {
			i.setImageBitmap(bm);
		}
		i.setAdjustViewBounds(true);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.browseImageDialog.dismiss();
	}


}
