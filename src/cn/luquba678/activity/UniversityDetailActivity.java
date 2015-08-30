package cn.luquba678.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.GradeLineAdapter;
import cn.luquba678.entity.CityMsg;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.GradeLine;
import cn.luquba678.entity.School;
import cn.luquba678.ui.HttpUtil;
import com.zhuchao.utils.ImageLoader;
import cn.luquba678.view.CircularImage;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuchao.http.Network;
import com.zhuchao.view_rewrite.ExpandableLayout;
import com.zhuchao.view_rewrite.LoadingDialog;

public class UniversityDetailActivity extends CommonActivity implements OnClickListener ,Runnable{

	private ImageLoader ima = new ImageLoader(this);
	private CircularImage logo;

	private TextView schoolNameTv, topTitle, introduce, school_type_text, mark_211, mark_985, school_province_text, tel_text, web_text;
	String formatA = "<a href=\"%s\">%s</a>";
	private School school;
	private Intent intent;
	private GradeLineAdapter GradeLineAdapter;
	private View is_yanjiusheng;

    private String school_id,grade_line_string;
    private ExpandableLayout intro,line,phone,homepage;

    private LoadingDialog loadingDialog;
    private ScrollView scrollView;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setText();
                    break;
                case 1:
                    break;
            }
            loadingDialog.stopProgressDialog();
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        }catch (Exception e){
            e.printStackTrace();
        }
		setContentView(R.layout.activity_university_detail);

        intent = getIntent();
        String schoolJson = intent.getStringExtra("schoolJson");
        school_id=intent.getStringExtra("school_id");
        /**
         * find view
         */
        scrollView=(ScrollView)findViewById(R.id.scroll);
        intro=(ExpandableLayout)findViewById(R.id.intro_ly);
        line=(ExpandableLayout)findViewById(R.id.line_ly);
        phone=(ExpandableLayout)findViewById(R.id.phone_ly);
        homepage=(ExpandableLayout)findViewById(R.id.homepage_ly);
        loadingDialog=new LoadingDialog(this);
		topTitle = (TextView) findViewById(R.id.top_text);
		topTitle.setText("大学简介");
		findViewById(R.id.top_back).setOnClickListener(this);
		logo = (CircularImage) findViewById(R.id.school_detail_logo);
        setOnClickLinstener(R.id.top_back, R.id.web_text);
		school = School.getSchoolFromJson(schoolJson);
		schoolNameTv = getView(R.id.school_name);
		introduce = getView(R.id.introduce);
		school_type_text = getView(R.id.school_type_text);
		web_text = getView(R.id.web_text);
		tel_text = getView(R.id.tel_text);
		school_province_text = getView(R.id.school_province_text);
		mark_211 = getView(R.id.is_211);
		mark_985 = getView(R.id.is_985);
		is_yanjiusheng = getView(R.id.is_yanjiusheng);

        /**
         * init text
         */
        if(Network.checkNetWorkState(this)){
            loadingDialog.startProgressDialog();
            new Thread(this).start();
        }else{
            Toast.makeText(UniversityDetailActivity.this, "未连接网络", Toast.LENGTH_SHORT).show();
        }
	}

    private void setText(){
        schoolNameTv.setText(school.getSchool_name());
        introduce.setText(Html.fromHtml(school.getIntro()));
        school_province_text.setText("所在省份:" + CityMsg.getShortNameFromId(school.getArea_id()).area_name);
        school_type_text.setText("院校类型:" + school.getType());
        ima.DisplayImage(school.getLogo(), logo);
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
        web_text.setText(Html.fromHtml(String.format(formatA, school.getWeb(), school.getWeb())));
        initGradeLine();

        line.onRefresh();
        phone.onRefresh();
        intro.onRefresh();
        homepage.onRefresh();
        scrollView.scrollTo(0,0);
    }
	private void initGradeLine() {
		ListView gradelinelistview = getView(R.id.gradelinelistview);
		ArrayList<GradeLine> gradeline = GradeLine.getListFromJson(grade_line_string);
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

    @Override
    public void run() {
        try {
            MultipartEntity entity = new MultipartEntity();
            SharedPreferences sharedPreferences = this.getSharedPreferences("luquba_login", Context.MODE_PRIVATE);
            entity.addPart("school_id", new StringBody(school_id));
            entity.addPart("stu_area_id", new StringBody(sharedPreferences.getString(School.HOME_AREA_ID, "110000")));

            JSONObject obj = JSONObject.parseObject(HttpUtil.postRequestEntity(Const.QUERY_SCHOOL_DETAIL, entity));
            Integer errcode = obj.getInteger("errcode");
            if (errcode == 0) {
                JSONObject school = obj.getJSONObject("school");
                UniversityDetailActivity.this.school=School.getSchoolFromJson(school.toString());
                JSONArray gradeline = obj.getJSONArray("gradeline");
                grade_line_string=gradeline.toString();
                handler.sendEmptyMessage(0);
            }else{
                handler.sendEmptyMessage(1);
            }
        } catch (Exception e) {
            handler.sendEmptyMessage(1);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
