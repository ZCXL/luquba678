package cn.luquba678.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.adapter.MajorListAdapter;
import cn.luquba678.activity.adapter.SchoolListAdapter;
import cn.luquba678.entity.CityMsg;
import cn.luquba678.entity.MatriculateMsg;
import cn.luquba678.entity.School;
import cn.luquba678.entity.TestMajorDate;
import cn.luquba678.service.LoadDataFromServer;
import cn.luquba678.utils.ImageLoader;
import cn.luquba678.view.CircularImage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MajorsDetailActivity extends CommonActivity implements
		OnClickListener, OnItemClickListener {

	private TextView title;
	ImageLoader ima = new cn.luquba678.utils.ImageLoader(this);
	private CircularImage logo;
	private ListView msgList;
	private ListView major_list;
	private ArrayList<TestMajorDate> tmds;
	private ArrayList<MatriculateMsg> majors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_majors_detail);
		title = (TextView) findViewById(R.id.top_text);
		findViewById(R.id.top_back).setOnClickListener(this);
		title.setText("对应专业");

		Intent intent = getIntent();
		String majorJson = intent.getStringExtra("majorJson");
		majors = MatriculateMsg.getListFromJson(majorJson);
		major_list = (ListView) findViewById(R.id.major_list);
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				handler.sendMessage(handler.obtainMessage(10, majors));
			}
		});

	}

	private MajorListAdapter adapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			adapter = new MajorListAdapter(MajorsDetailActivity.this,
					(ArrayList<MatriculateMsg>) msg.obj,
					R.layout.majors_detail_item);
			major_list.setAdapter(adapter);
			major_list.setOnItemClickListener(MajorsDetailActivity.this);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		View v = view.findViewById(R.id.major_content);
		if (v.getVisibility() == View.GONE)
			v.setVisibility(View.VISIBLE);
		else
			v.setVisibility(View.GONE);
	}

}
