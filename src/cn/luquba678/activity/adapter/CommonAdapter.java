package cn.luquba678.activity.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import cn.luquba678.R;
import cn.luquba678.entity.People;
import cn.luquba678.entity.TestMajorDate;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class CommonAdapter<T> extends BaseAdapter {

	Context context;
	List<T> dates;
	private LayoutInflater inflater;
	int layoutId;

	public CommonAdapter(Context context, List<T> dates, int layoutId) {
		super();
		this.context = context;
		this.dates = dates;
		this.inflater = LayoutInflater.from(context);
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		if (dates != null)
			return dates.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return dates.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void changeDate(List<T> obj) {
		if (obj != null && obj.size() > 0)
			this.dates = obj;
		else
			this.dates = new ArrayList<T>();
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.getViewHolder(context, convertView, parent, position, layoutId);
		setViews(holder, dates.get(position), position);
		return holder.getConvertView();
	}

	public abstract void setViews(ViewHolder holder, T t, int position);

	public void changeDateInThread(final List<T> obj) {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				handlerChange.sendMessage(handlerChange.obtainMessage(10, obj));
			}
		});

	}

	private Handler handlerChange = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			CommonAdapter.this.changeDate((List<T>) msg.obj);
		}
	};
}
