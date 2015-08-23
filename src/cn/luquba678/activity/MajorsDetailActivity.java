package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.adapter.MajorListAdapter;
import cn.luquba678.entity.MatriculateMsg;

public class MajorsDetailActivity extends CommonActivity implements OnClickListener {

	private TextView title;
	private ListView major_list;
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
			adapter = new MajorListAdapter(MajorsDetailActivity.this, (ArrayList<MatriculateMsg>) msg.obj, R.layout.majors_detail_item);
			major_list.setAdapter(adapter);
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


}
