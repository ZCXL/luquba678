package cn.luquba678.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.luquba678.R;
import cn.luquba678.activity.adapter.CommonAdapter;
import cn.luquba678.activity.adapter.GradeLineAdapter;
import cn.luquba678.activity.adapter.SchoolListAdapter;
import cn.luquba678.activity.adapter.ViewHolder;
import cn.luquba678.entity.CityMsg;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.GradeLine;
import cn.luquba678.entity.MatriculateMsg;
import cn.luquba678.entity.School;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.utils.CacheUtil;
import cn.luquba678.utils.ImageLoader;
import cn.luquba678.view.CircularImage;

import com.baidu.navisdk.util.common.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UniversityDetailActivity extends CommonActivity implements
		OnClickListener {

	private ImageLoader ima = new cn.luquba678.utils.ImageLoader(this);
	private CircularImage logo;

	private TextView schoolNameTv, topTitle, introduce, school_type_text,
			mark_211, mark_985, school_province_text, tel_text, web_text;
	String formatA = "<a href=\"%s\">%s</a>";
	private School school;
	private Intent intent;
	private GradeLineAdapter GradeLineAdapter;
	private View is_yanjiusheng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_university_detail);
		topTitle = (TextView) findViewById(R.id.top_text);
		topTitle.setText("大学简介");
		findViewById(R.id.top_back).setOnClickListener(this);
		logo = (CircularImage) findViewById(R.id.school_detail_logo);
		// String schoolname = intent.getStringExtra("schoolName");
		intent = getIntent();
		String schoolJson = intent.getStringExtra("schoolJson");
		school = School.getSchoolFromJson(schoolJson);
		schoolNameTv = getView(R.id.school_name);
		schoolNameTv.setText(school.getSchool_name());
		introduce = getView(R.id.introduce);
		school_type_text = getView(R.id.school_type_text);
		web_text = getView(R.id.web_text);
		tel_text = getView(R.id.tel_text);
		school_province_text = getView(R.id.school_province_text);
		mark_211 = getView(R.id.is_211);
		mark_985 = getView(R.id.is_985);
		is_yanjiusheng = getView(R.id.is_yanjiusheng);
		introduce.setText(Html.fromHtml(school.getIntro()));
		school_province_text.setText("所在省份:"
				+ CityMsg.getShortNameFromId(school.getArea_id()).area_name);
		school_type_text.setText("院校类型:" + school.getType());
		ima.DisplayImage(school.getLogo(), logo, false);
		// msgList = (ListView) findViewById(R.id.msglist);
		if (school.getIs_211() == 1) {
			mark_211.setVisibility(View.VISIBLE);
		} else
			mark_211.setVisibility(View.GONE);
		if (school.getIs_985() == 1)
			mark_985.setVisibility(View.VISIBLE);
		else
			mark_985.setVisibility(View.GONE);
		if (school.getIs_Yanjiusheng() == 1)
			is_yanjiusheng.setVisibility(View.VISIBLE);
		else
			is_yanjiusheng.setVisibility(View.GONE);
		tel_text.setText(school.getTel());
		web_text.setText(Html.fromHtml(String.format(formatA, school.getWeb(),
				school.getWeb())));
		setOnClickLinstener(R.id.top_back, R.id.phone, R.id.web_text);
		initGradeLine();
	}

	private void initGradeLine() {
		String gradelineJson = intent.getStringExtra("gradeline");
		
		ListView gradelinelistview = getView(R.id.gradelinelistview);
		ArrayList<GradeLine> gradeline = GradeLine.getListFromJson(gradelineJson);
		 GradeLineAdapter = new GradeLineAdapter(this, gradeline, R.layout.grade_line_item_schooldetail) ;
		gradelinelistview.setAdapter(GradeLineAdapter);
		
	}
	public void setTextColor(int color, TextView... views) {
		for (int i = 0; i < views.length; i++) {
			views[i].setTextColor(color);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.phone:
			// toast("打电话");
			String phoneno = school.getTel();
			if (phoneno.contains("，")) {
				phoneno = phoneno.substring(0, phoneno.indexOf("，"));
				Log.i("split打电话", phoneno);
			}
			if (StringUtils.isEmpty(phoneno)) {
				Toast.makeText(getApplicationContext(), "没有电话号码",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phoneno));
				Log.i("打电话", phoneno);
				startActivity(intent);
			}
			break;
		case R.id.top_back:
			this.finish();
			break;
		case R.id.web_text:
			Uri uri = Uri.parse(school.getWeb());
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			break;

		default:
			break;
		}
	}

}
