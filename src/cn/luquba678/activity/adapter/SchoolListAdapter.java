package cn.luquba678.activity.adapter;

import java.util.List;
import com.baidu.navisdk.util.common.StringUtils;
import com.zhuchao.utils.ImageLoader;

import cn.luquba678.R;
import cn.luquba678.activity.MajorsDetailActivity;
import cn.luquba678.activity.UniversityDetailActivity;
import cn.luquba678.entity.School;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SchoolListAdapter extends CommonAdapter<School> {
	private ImageLoader ima;
	private SharedPreferences sharedPreferences;
	private String kelei;
	public SchoolListAdapter(Context context, List<School> cms, int layoutId) {
		super(context, cms, layoutId);
		this.ima = new ImageLoader(context);
		ima.setStub_id(R.drawable.new_city_default);
		sharedPreferences = context.getSharedPreferences("luquba_login", Context.MODE_PRIVATE);
		kelei=sharedPreferences.getString(School.KELEI,"1");
	}

	class OnclickSchoolInfo implements OnClickListener {
		School school;

		public OnclickSchoolInfo(School school) {
			this.school = school;
		}

		@Override
		public void onClick(View v) {
            Intent intent = new Intent(context, UniversityDetailActivity.class);
            intent.putExtra("school_id",String.valueOf(school.getSchool_id()));
			intent.putExtra("kelei",kelei);
            context.startActivity(intent);
		}
	}

	/**
	 * 点击进入
	 * 
	 * @author Administrator
	 *
	 */
	class OnclickMajorInfo implements OnClickListener {
		School school;
		public OnclickMajorInfo(School school) {
			this.school = school;
		}

		@Override
		public void onClick(View v) {
            Intent intent = new Intent(context, MajorsDetailActivity.class);
            intent.putExtra("school_id", school.getSchool_id().toString());
			intent.putExtra("kelei",kelei);
            context.startActivity(intent);
		}

	}

	public void setViews(ViewHolder holder, School school, int position) {
		TextView school_name = holder.getView(R.id.school_name);
		TextView major = holder.getView(R.id.major);
		TextView pici=holder.getView(R.id.pici);
		LinearLayout lin = holder.getView(R.id.school_info);
		ImageView school_logo = holder.getView(R.id.school_logo);
		school_logo.setImageBitmap(null);
		school_name.setText(school.getSchool_name());
		String number=school.getPici().substring(2,3);
		if(number.equals("1"))
			number="一";
		else if(number.equals("2"))
			number="二";
		else if(number.equals("3"))
			number="三";
		String pici_string=school.getPici().substring(0,1)+number;
		pici.setText(pici_string);
		// w.school_logo.setImageResource(R.drawable.ic_search_big);
		TextView mark_211 = holder.getView(R.id.is_211);
		TextView mark_985 = holder.getView(R.id.is_985);
		if (school.getIs_211() == 1) {
			mark_211.setVisibility(View.VISIBLE);
		} else
			mark_211.setVisibility(View.GONE);
		if (school.getIs_985() == 1)
			mark_985.setVisibility(View.VISIBLE);
		else
			mark_985.setVisibility(View.GONE);
		ima.DisplayImage(school.getLogo(),school_logo);
		lin.setOnClickListener(new OnclickSchoolInfo(school));
		TextView school_type_text = holder.getView(R.id.school_type_text);
		String type = school.getType();
		if (StringUtils.isNotEmpty(type)) {
			school_type_text.setVisibility(View.VISIBLE);
			school_type_text.setText(school.getType());
		} else {
			school_type_text.setVisibility(View.GONE);
		}

		TextView school_area = holder.getView(R.id.school_area);
		school_area.setText(school.getAreaName());
		major.setOnClickListener(new OnclickMajorInfo(school));
	}
}
