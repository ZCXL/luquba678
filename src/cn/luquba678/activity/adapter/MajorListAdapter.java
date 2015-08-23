package cn.luquba678.activity.adapter;

import java.util.ArrayList;
import cn.luquba678.R;
import cn.luquba678.entity.GradeLine;
import cn.luquba678.entity.MatriculateMsg;
import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuchao.utils.LayoutUtils;

public class MajorListAdapter extends CommonAdapter<MatriculateMsg> {

	public MajorListAdapter(Context context, ArrayList<MatriculateMsg> tmds, int layoutId) {
		super(context, tmds, layoutId);
	}

	@Override
	public void setViews(ViewHolder holder, MatriculateMsg tmd, int p) {
		TextView major_name = holder.getView(R.id.major_name);
		ListView gradeLineListView = holder.getView(R.id.gradelinelistview);

		CommonAdapter<GradeLine> adapter = new GradeLineAdapter(context, tmd.getGradeline(), R.layout.grade_line_item);
		major_name.setText(tmd.getMajor_name());
		gradeLineListView.setAdapter(adapter);
		LayoutUtils.setListViewHeightBasedOnChildren(gradeLineListView);
	}

	public void setTextColor(int color, TextView... views) {
		for (int i = 0; i < views.length; i++) {
			views[i].setTextColor(color);
		}
	}

}
