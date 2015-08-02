package cn.luquba678.activity.adapter;

import java.util.List;
import java.util.concurrent.Executors;

import com.baidu.navisdk.util.common.StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.service.LoadDataFromServer.DataCallBack;
import cn.luquba678.utils.DateUtils;
import cn.luquba678.utils.ImageLoader;
import cn.luquba678.utils.ImageUtil;
import cn.luquba678.utils.ScreenUtils;

public class StoryAdapter extends CommonAdapter<News> {

	private ImageLoader ima;
	private ImageGetter imageGetter;
	private LoadDataFromServer task;

	public StoryAdapter(Context context, List<News> dates, int layoutId) {
		super(context, dates, layoutId);
		this.ima = new cn.luquba678.utils.ImageLoader(context);
		imageGetter = ImageUtil.getImageGetterFromUrl(ima, context);
	}

	@Override
	public void setViews(ViewHolder holder, News t, int p) {
		TextView title = holder.getView(R.id.story_title);
		// TextView content = holder.getView(R.id.story_content);
		TextView origin = holder.getView(R.id.come_from);
		TextView creat_date = holder.getView(R.id.creat_date);
		ImageView image = holder.getView(R.id.image);
		String url = t.getPic();
		if (layoutId == R.layout.activity_funny_cell) {
			if (StringUtils.isNotEmpty(url)) {
					image.setVisibility(View.VISIBLE);
					ima.DisplayImage(url, image, false,ScreenUtils.getScreenWidth(context));
					//image.setImageBitmap(bm);
			} else {
				image.setVisibility(View.GONE);
			}
			//ima.DisplayImage(url, image, true,ScreenUtils.getScreenWidth(context),500);
			TextView story_content = holder.getView(R.id.story_content);
			//Spanned spa= Html.fromHtml(t.getContent());
			story_content.setText(Html.fromHtml(t.getContent()));
		}else{
			if (StringUtils.isNotEmpty(url)) {
				Bitmap bm = ImageLoader.getBitmap(url);
				if (bm != null) {
					image.setVisibility(View.VISIBLE);
					bm =ImageUtil.centerSquareScaleBitmap(bm, 200);
					image.setImageBitmap(bm);
				} else {
					image.setVisibility(View.GONE);
				}
			} else {
				image.setVisibility(View.GONE);
			}
			
		}

		/*
		 * task.getData(new DataCallBack() {
		 * 
		 * @Override public void onDataCallBack(int what, Object data) {
		 * 
		 * story_content.setText(Html.fromHtml(data.toString()));
		 * 
		 * } });
		 */

		// story_content.setText(Html.fromHtml(source, imageGetter, null));
		// webview.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		// webview.loadUrl("http://www.b.com/");
		// ImageView preview = holder.getView(R.id.story_previewimage);
		title.setText(t.getTitle());
		origin.setText(t.getOrigin());
		creat_date.setText(t.getCreatetime());
		// content.setText(t.getContent());
		/*
		 * if (StringUtils.isNotEmpty(t.getPic())) {
		 * preview.setVisibility(View.VISIBLE); ima.DisplayImage(t.getPic(),
		 * preview, false); } else { preview.setVisibility(View.GONE); }
		 */
	}
}
