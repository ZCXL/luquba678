package cn.luquba678.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.luquba678.R;
import cn.luquba678.entity.CityMsg;
import cn.luquba678.entity.GradeLine;
import cn.luquba678.entity.MatriculateMsg;
import cn.luquba678.entity.TestMajorDate;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MajorListAdapter extends CommonAdapter<MatriculateMsg> {

	public MajorListAdapter(Context context, ArrayList<MatriculateMsg> tmds,
			int layoutId) {
		super(context, tmds, layoutId);
		OnClickListener l = new zhankaiClick(null);
	}

	class zhankaiClick implements OnClickListener {
		private View tv;

		public zhankaiClick(View tv) {
			super();
			this.tv = tv;
		}

		public void setTv(View tv) {
			this.tv = tv;
		}

		@Override
		public void onClick(View v) {
			if (tv.getVisibility() == View.GONE)
				tv.setVisibility(View.VISIBLE);
			else
				tv.setVisibility(View.GONE);
		}

	}

	@Override
	public void setViews(ViewHolder holder, MatriculateMsg tmd, int p) {
		// TextView major_content = holder.getView(R.id.major_content);
		TextView major_name = holder.getView(R.id.major_name);
		holder.getView(R.id.click_zhankai).setOnClickListener(
				new zhankaiClick(holder.getView(R.id.major_content)));
		ListView gradelinelistview = holder.getView(R.id.gradelinelistview);

		CommonAdapter<GradeLine> adapter = new GradeLineAdapter(
				context, tmd.getGradeline(), R.layout.grade_line_item);
		//View headview = View.inflate(context, R.layout.grade_line_item, null);

		//gradelinelistview.addHeaderView(headview, null, false);
		major_name.setText(tmd.getMajor_name());
		gradelinelistview.setAdapter(adapter);

		// major_content.setText(tmd.getGradeline());

	}

	public void setTextColor(int color, TextView... views) {
		for (int i = 0; i < views.length; i++) {
			views[i].setTextColor(color);
		}
	}

}
