package cn.luquba678.activity.adapter;

import java.util.List;

import com.baidu.navisdk.util.common.StringUtils;
import com.zhuchao.utils.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.entity.News;

public class StoryAdapter extends CommonAdapter<News> {

	private ImageLoader ima;

	public StoryAdapter(Context context, List<News> dates, int layoutId) {
		super(context, dates, layoutId);
		this.ima = new ImageLoader(context);
	}

	@Override
	public void setViews(ViewHolder holder, News t, int p) {

		//get content of image

		TextView title = holder.getView(R.id.story_title);
		TextView origin = holder.getView(R.id.come_from);
		TextView create_date = holder.getView(R.id.creat_date);
		ImageView image = holder.getView(R.id.image);

		//load image
		String url = t.getPic();

		if (layoutId == R.layout.activity_funny_cell) {
			TextView story_content = holder.getView(R.id.story_content);
			if(StringUtils.isEmpty(url)){
				story_content.setVisibility(View.VISIBLE);
				image.setVisibility(View.GONE);
				String content=jsonToString(t.getContent());
                Log.d("zhuchao", content);
				/**
				 * decode twice
				 */
				story_content.setText(content);
			}else{
				story_content.setVisibility(View.GONE);
				image.setVisibility(View.VISIBLE);
				image.setTag(url);
				ima.DisplayImage(url,image);
			}
		}else{
			if (StringUtils.isNotEmpty(url)) {
				image.setVisibility(View.VISIBLE);
				image.setTag(url);
				ima.DisplayImage(url,image);
			} else {
				image.setVisibility(View.GONE);
			}

		}

		title.setText(t.getTitle());
		origin.setText(t.getOrigin());
		create_date.setText(t.getCreatetime());
	}
    private String jsonToString(String content){
        String[]contents=content.split("\r\n");
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<contents.length;i++)
			if(i==contents.length-1)
				builder.append(contents[i]);
			else
				builder.append(contents[i]+"\n");
        return builder.toString();
    }
}
