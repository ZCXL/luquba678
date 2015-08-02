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
import cn.luquba678.entity.News;
import cn.luquba678.utils.FileCache;
import cn.luquba678.utils.ImageLoader;

public class CommentAdapter extends CommonAdapter<News>  {
	ImageLoader imageLoader;
	public CommentAdapter(Context context, List<News> dates, int layoutId) {
		super(context, dates, layoutId);
		imageLoader = new ImageLoader(context);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public void setViews(ViewHolder holder, News t, int position) {
		ImageView head_img = holder.getView(R.id.head_img);
		TextView nick_name = holder.getView(R.id.nick_name);
		TextView comment_content = holder.getView(R.id.comment_content);
		Bitmap bm = null;
		imageLoader.DisplayImage(t.getHeadpic(), head_img, false);
		nick_name.setText(t.getNickname());
		comment_content.setText(t.getContent());
		
	}



}
