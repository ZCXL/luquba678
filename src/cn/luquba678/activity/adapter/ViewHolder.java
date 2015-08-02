package cn.luquba678.activity.adapter;

import android.content.Context;
import android.text.Layout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder {
	private SparseArray<View> views;
	public View mConvertView;
	public int position;

	public ViewHolder(Context context, ViewGroup parent, int position,
			int layoutId) {
		super();
		this.position = position;
		this.views = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public <T extends View> T getView(int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T) view;
	}

	public static ViewHolder getViewHolder(Context context, View convertView,
			ViewGroup parent, int position, int layoutId) {
		if (convertView == null) {
			return new ViewHolder(context, parent, position, layoutId);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.position = position;
			return holder;
		}
	}

	public View getConvertView() {
		return this.mConvertView;
	}
}
