package cn.luquba678.activity.adapter;

import java.util.ArrayList;

import cn.luquba678.R;
import cn.luquba678.entity.CityMsg;
import android.content.Context;
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
