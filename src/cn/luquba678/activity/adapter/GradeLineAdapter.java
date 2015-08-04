package cn.luquba678.activity.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.entity.GradeLine;

public class GradeLineAdapter extends CommonAdapter<GradeLine>{
	public GradeLineAdapter(Context context, List<GradeLine> dates, int layoutId) {
		super(context, dates, layoutId);
	}

	@Override
	public void setViews(ViewHolder holder, GradeLine t, int position) {
		TextView year = holder.getView(R.id.year);
		TextView avgscore = holder.getView(R.id.avgscore);
		TextView admitNum = holder.getView(R.id.admitNum);
		TextView maxscore = holder.getView(R.id.maxscore);
		TextView kelei = holder.getView(R.id.kelei);
		
		if(layoutId==R.layout.grade_line_item_schooldetail){
			TextView pici = holder.getView(R.id.pici);
			pici.setText(t.getPici());
			pici.setTextColor(context.getResources().getColor(R.color.gray_9));
		}
		holder.mConvertView.setBackgroundColor(context.getResources().getColor(R.color.gray_f));
		year.setText(t.getYear().toString());
		avgscore.setText(gradeFormat(t.getAveScore().toString()));
		admitNum.setText(gradeFormat(t.getAdmitNum().toString()));
		maxscore.setText(gradeFormat(t.getMaxScore().toString()));
		kelei.setText(t.getKeleiName());
		setTextColor(context.getResources().getColor(R.color.gray_9), year, avgscore, admitNum, maxscore, kelei);

	}public void setTextColor(int color, TextView... views) {
		for (int i = 0; i < views.length; i++) {
			views[i].setTextColor(color);
		}
	}

	/**
	 *
	 * @param grade
	 * @return
	 */
	public static String gradeFormat(String grade){
		if(grade.length()==1)
			return grade+"    ";
		if(grade.length()==2)
			return grade+"  ";
		return grade;
	}
}
