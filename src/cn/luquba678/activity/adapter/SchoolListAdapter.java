package cn.luquba678.activity.adapter;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;
import com.zhuchao.utils.ImageLoader;
import com.zhuchao.view_rewrite.LoadingDialog;

import cn.luquba678.R;
import cn.luquba678.activity.MajorsDetailActivity;
import cn.luquba678.activity.UniversityDetailActivity;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.School;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.CacheUtil;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SchoolListAdapter extends CommonAdapter<School> {
	ImageLoader ima;
	private LoadingDialog loadingDialog;

	public SchoolListAdapter(Context context, List<School> cms, int layoutId) {
		super(context, cms, layoutId);
		this.ima = new ImageLoader(context);
		ima.setStub_id(R.drawable.new_city_default);
		loadingDialog=new LoadingDialog(context);
	}

	class OnclickSchoolInfo implements OnClickListener {
		School school;

		public OnclickSchoolInfo(School school) {
			this.school = school;
		}

		@Override
		public void onClick(View v) {
			loadingDialog.startProgressDialog();
			try {
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("school_id", new StringBody(school.getSchool_id().toString()));
				entity.addPart("stu_area_id", new StringBody(school.getArea_id().toString()));

				JSONObject obj = JSONObject.parseObject(HttpUtil.postRequestEntity(Const.QUERY_SCHOOL_DETAIL, entity));
				CacheUtil.writeText(new File(CacheUtil.getCache(), "schoolll.txt"),obj.toString());

				Integer errcode = obj.getInteger("errcode");
				if (errcode == 0) {
					Intent intent = new Intent(context, UniversityDetailActivity.class);
					JSONObject school = obj.getJSONObject("school");
					JSONArray gradeline = obj.getJSONArray("gradeline");
					intent.putExtra("schoolJson", school.toJSONString());
					intent.putExtra("gradeline", gradeline.toJSONString());
					loadingDialog.stopProgressDialog();
					context.startActivity(intent);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				loadingDialog.stopProgressDialog();
				e.printStackTrace();
			}
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
		private SharedPreferences sharedPreferences;
		private Editor editor;

		public OnclickMajorInfo(School school) {
			this.school = school;
		}

		@Override
		public void onClick(View v) {
			loadingDialog.startProgressDialog();
			try {
				MultipartEntity entity = new MultipartEntity();

				sharedPreferences = context.getSharedPreferences("luquba_login", Context.MODE_PRIVATE);
				editor = sharedPreferences.edit();// 获取编辑器
				entity.addPart("school_id", new StringBody(school.getSchool_id().toString()));
				entity.addPart("stu_area_id", new StringBody(sharedPreferences.getString(School.HOME_AREA_ID, "")));

				entity.addPart("kelei", new StringBody(sharedPreferences.getString(School.KELEI, "")));
				entity.addPart("grade", new StringBody(sharedPreferences.getString(School.GRADE, "")));

				JSONObject obj = JSONObject.parseObject(HttpUtil.postRequestEntity(Const.QUERY_MAJOR, entity));
				Integer errcode = obj.getInteger("errcode");
				if (errcode == 0) {
					JSONArray major = obj.getJSONArray("data");

					Intent intent = new Intent(context, MajorsDetailActivity.class);
					intent.putExtra("majorJson", major.toJSONString());
					context.startActivity(intent);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			loadingDialog.stopProgressDialog();
		}

	}

	public void setViews(ViewHolder holder, School school, int position) {
		TextView school_name = holder.getView(R.id.school_name);
		TextView major = holder.getView(R.id.major);
		LinearLayout lin = holder.getView(R.id.school_info);
		ImageView school_logo = holder.getView(R.id.school_logo);
		school_logo.setImageBitmap(null);
		school_name.setText(school.getSchool_name());
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
