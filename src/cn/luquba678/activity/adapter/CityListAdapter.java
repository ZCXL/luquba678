package cn.luquba678.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.luquba678.R;
import cn.luquba678.entity.CityMsg;
import cn.luquba678.entity.TestMajorDate;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CityListAdapter extends CommonAdapter<CityMsg> {

	public CityListAdapter(Context context, ArrayList<CityMsg> cms, int layoutId) {
		super(context, cms, layoutId);
	}

	@Override
	public void setViews(ViewHolder holder, CityMsg t, int p) {
		TextView city_name = holder.getView(R.id.city_name);
		city_name.setText(t.getArea_name());

	}

}
