package cn.luquba678.activity.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.luquba678.R;
import cn.luquba678.entity.CollectItem;
import cn.luquba678.entity.Const;
import cn.luquba678.utils.ImageLoader;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectListAdapter extends CommonAdapter<CollectItem> {
	public CollectListAdapter(Context context, List<CollectItem> dates,
			int layoutId) {
		super(context, dates, layoutId);
		imageLoader = new ImageLoader(context);
		this.context = context;
		collectItem_list = dates;
	}

	private List<CollectItem> collectItem_list;
	private Context context;
	private ViewHolder holder;
	private boolean needShowCheckBox = false;
	private int clickPostion = -1;
	private ImageLoader imageLoader;
    private List<String> checkFlag = new ArrayList<String>();;
	@Override
	public View getView(int postion, View convertView, ViewGroup arg2) {
		Log.i("wyb", "postion is  " + postion +"");
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_collect, null);
			holder = new ViewHolder();
			holder.collect_img = (ImageView) convertView
					.findViewById(R.id.collect_img);
			holder.collect_lable = (TextView) convertView
					.findViewById(R.id.collect_lable);
			holder.collect_title = (TextView) convertView
					.findViewById(R.id.collect_title);
			holder.collect_date = (TextView) convertView
					.findViewById(R.id.collect_date);
			holder.collect_check = (CheckBox) convertView
					.findViewById(R.id.collect_check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String imgUrl = Const.BASE_URL + "/"
				+ collectItem_list.get(postion).getCollect_imgUrl();
		imageLoader.DisplayImage(imgUrl, holder.collect_img, false,300);
		String lable = collectItem_list.get(postion).getCollect_type();
		if (lable.equals("1")) {
			holder.collect_lable.setText("励志故事");
		} else if (lable.equals("2")) {
			holder.collect_lable.setText("状元心得");
		} else if (lable.equals("3")) {
			holder.collect_lable.setText("校花校草");
		} else if (lable.equals("4")) {
			holder.collect_lable.setText("校园趣事");
		} else if (lable.equals("5")) {
			holder.collect_lable.setText("搞笑段子");
		} else if (lable.equals("6")) {
			holder.collect_lable.setText("内涵图");
		}
		holder.collect_title.setText(collectItem_list.get(postion)
				.getCollect_title());
		holder.collect_date.setText(collectItem_list.get(postion)
				.getCollect_date());
		holder.collect_check.setVisibility(View.GONE);
		if (needShowCheckBox) {
			holder.collect_check.setVisibility(View.VISIBLE);
			if (postion == clickPostion) {
				if (holder.collect_check.isChecked()) {
					holder.collect_check.setChecked(false);
					checkFlag.remove(postion+"");
				} else {
					Log.i("wyb", "checkFlag.add");
					checkFlag.add(postion+"");
					holder.collect_check.setChecked(true);
				}
				clickPostion = -1;
			}
		} else {
			holder.collect_check.setChecked(false);
		}
		return convertView;
	}

	public final class ViewHolder {
		public ImageView collect_img;
		public TextView collect_title;
		public TextView collect_lable;
		public TextView collect_date;
		public CheckBox collect_check;
	}

	public void showCheckBox(boolean isNeedShow) {
		needShowCheckBox = isNeedShow;
		notifyDataSetChanged();
	}

	public void deleteItem() {
		for(int i=0;i<checkFlag.size();i++){
			collectItem_list.remove(i);
		}
		notifyDataSetChanged();
		checkFlag.clear();
	}

	public void refershCheckBoxState(int position) {
		needShowCheckBox = true;
		clickPostion = position;
		notifyDataSetChanged();
	}

	@Override
	public void setViews(cn.luquba678.activity.adapter.ViewHolder holder,
			CollectItem t, int position) {
	}


}
