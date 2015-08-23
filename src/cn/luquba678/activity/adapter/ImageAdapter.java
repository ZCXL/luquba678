package cn.luquba678.activity.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zhuchao.bean.Wish;
import com.zhuchao.utils.ImageLoader;

import cn.luquba678.R;
import cn.luquba678.activity.BrowseImageDialog;

public class ImageAdapter extends CommonAdapter<Wish> implements OnClickListener  {
	ImageLoader imageLoader;
	BrowseImageDialog browseImageDialog;
	public ImageAdapter(Context context, ArrayList<Wish> dates, int layoutId,BrowseImageDialog browseImageDialog) {
		super(context, dates, layoutId);
		imageLoader = new ImageLoader(context);
		this.browseImageDialog= browseImageDialog;
		// TODO Auto-generated constructor stub
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public void setViews(ViewHolder holder, Wish wish, int position) {
		ImageView i = holder.getView(R.id.image_view);

		i.setTag(wish.getPic());
		imageLoader.DisplayImage(wish.getPic(),i);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.browseImageDialog.dismiss();
	}

}
