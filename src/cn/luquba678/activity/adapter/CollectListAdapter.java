package cn.luquba678.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.luquba678.R;
import cn.luquba678.entity.CollectItem;
import cn.luquba678.entity.Const;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.zhuchao.utils.ImageLoader;

public class CollectListAdapter extends CommonAdapter<CollectItem> {

    private List<CollectItem> collectItem_list;
    private HashMap<Integer,Boolean>checkState;
    private Context context;

    private boolean needShowCheckBox = false;
    private int clickPosition = -1;
    private ImageLoader imageLoader;
    private List<Integer> checkFlag = new ArrayList<Integer>();

    private OnDeleteListener onDeleteListener;
	public CollectListAdapter(Context context, List<CollectItem> dates, int layoutId) {
		super(context, dates, layoutId);
		imageLoader = new ImageLoader(context);
		this.context = context;
		collectItem_list = dates;
        checkState=new HashMap<Integer,Boolean>();
	}

    public OnDeleteListener getOnDeleteListener() {
        return onDeleteListener;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    @Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        CollectItem collectItem=collectItem_list.get(position);

		Log.i("wyb", "position is  " + position +"");
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_collect, null);
			holder = new ViewHolder();

			holder.collect_img = (ImageView) convertView.findViewById(R.id.collect_img);
			holder.collect_label = (TextView) convertView.findViewById(R.id.collect_lable);
			holder.collect_title = (TextView) convertView.findViewById(R.id.collect_title);
			holder.collect_date = (TextView) convertView.findViewById(R.id.collect_date);
			holder.collect_check = (CheckBox) convertView.findViewById(R.id.collect_check);

            holder.right_item=(RelativeLayout)convertView.findViewById(R.id.item_right);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


        /**
         *while click right block,delete current view
         */
        holder.right_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });
        /**
         * set image
         */
		String imgUrl = collectItem.getPic();
		if(StringUtils.isNotEmpty(imgUrl)) {
			holder.collect_img.setTag(imgUrl);
			imageLoader.DisplayImage(imgUrl, holder.collect_img);
		}

        /**
         * set type
         */
		String label = collectItem.getType();
		holder.collect_label.setText(label);

        /**
         * set title
         */
		holder.collect_title.setText(collectItem.getTitle());
		holder.collect_date.setText(collectItem.getCollect_time());
		holder.collect_check.setVisibility(View.GONE);

        /**
         * update check state
         */
		if (needShowCheckBox) {
			holder.collect_check.setVisibility(View.VISIBLE);
			if (position == clickPosition) {
				if (holder.collect_check.isChecked()) {
					holder.collect_check.setChecked(false);
					checkState.put(position, false);
				} else {
					checkState.put(position,true);
					holder.collect_check.setChecked(true);
				}
				clickPosition = -1;
			}
		} else {
			holder.collect_check.setChecked(false);
		}
		return convertView;
	}

    /**
     * view holder
     */
	public final class ViewHolder {
		public ImageView collect_img;
		public TextView collect_title;
		public TextView collect_label;
		public TextView collect_date;
		public CheckBox collect_check;

        public RelativeLayout right_item;
	}

	/**
	 * change state of check box
	 * @param isNeedShow
	 */
	public void showCheckBox(boolean isNeedShow) {
		needShowCheckBox = isNeedShow;
		checkState.clear();
		notifyDataSetChanged();
	}

	/**
	 * delete certain position item
	 * @param position
	 */
    public void deleteItem(int position){
        /**
         * get items that are selected
         */
        ArrayList<CollectItem>temp=new ArrayList<CollectItem>();

        temp.add(collectItem_list.get(position));

        /**
         * roll back to delete data
         */
        if(onDeleteListener!=null)
            onDeleteListener.onDelete(temp);
        /**
         * clear data
         */
        checkState.clear();
    }
    /**
     * delete item that are selected
     */
	public void deleteItem() {
        /**
         * get items that are selected
         */
        ArrayList<CollectItem>temp=new ArrayList<CollectItem>();
        Iterator i =checkState.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            int key = (Integer)entry.getKey();
            boolean val = (Boolean)entry.getValue();
            if(key<collectItem_list.size()&&key>=0&&val){
                temp.add(collectItem_list.get(key));
            }
        }

        /**
         * roll back to delete data
         */
        if(onDeleteListener!=null)
            onDeleteListener.onDelete(temp);
        /**
         * clear data
         */
        checkState.clear();
	}

    /**
     * while one item is selected,invalidate all item
     * @param position
     */
	public void refreshCheckBoxState(int position) {
		needShowCheckBox = true;
		clickPosition = position;
		notifyDataSetChanged();
	}

	@Override
	public void setViews(cn.luquba678.activity.adapter.ViewHolder holder, CollectItem t, int position) {
        //not used
	}

    /**
     * delete interface
     */
    public interface OnDeleteListener{
        /**
         * use this function to delete items that are selected
         * @param list
         */
        void onDelete(ArrayList<CollectItem>list);
    }

}
