package cn.luquba678.activity;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuchao.http.Network;
import com.zhuchao.view_rewrite.LoadingDialog;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.MajorListAdapter;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.MatriculateMsg;
import cn.luquba678.entity.School;
import cn.luquba678.ui.HttpUtil;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

public class MajorsDetailActivity extends CommonActivity implements OnClickListener,Runnable{

	private TextView title;
	private ListView major_list;
	private ArrayList<MatriculateMsg> majors;
	private String school_id,kelei;
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_majors_detail);
		title = (TextView) findViewById(R.id.top_text);
		findViewById(R.id.top_back).setOnClickListener(this);
		title.setText("对应专业");
		loadingDialog=new LoadingDialog(this);

		Intent intent = getIntent();
		school_id=intent.getStringExtra("school_id");
		kelei=intent.getStringExtra("kelei");
		major_list = (ListView) findViewById(R.id.major_list);
        if(Network.checkNetWorkState(this)) {
            loadingDialog.startProgressDialog();
            new Thread(this).start();
        }else {

        }
	}

	private MajorListAdapter adapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0:
					majors = MatriculateMsg.getListFromJson((String)msg.obj);
					Executors.newSingleThreadExecutor().execute(new Runnable() {
						@Override
						public void run() {
							handler.sendMessage(handler.obtainMessage(1, majors));
						}
					});
					break;
				case 1:
					adapter = new MajorListAdapter(MajorsDetailActivity.this, (ArrayList<MatriculateMsg>) msg.obj, R.layout.majors_detail_item);
					major_list.setAdapter(adapter);
                    loadingDialog.stopProgressDialog();
					break;
                case 2:
                    loadingDialog.stopProgressDialog();
                    break;
			}
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
	public void run() {
        try {
            MultipartEntity entity = new MultipartEntity();

            SharedPreferences sharedPreferences = this.getSharedPreferences("luquba_login", Context.MODE_PRIVATE);
            entity.addPart("school_id", new StringBody(school_id));
            entity.addPart("stu_area_id", new StringBody(sharedPreferences.getString(School.HOME_AREA_ID, "")));

            entity.addPart("kelei", new StringBody(sharedPreferences.getString(School.KELEI, "")));
            entity.addPart("grade", new StringBody(sharedPreferences.getString(School.GRADE, "")));

            JSONObject obj = JSONObject.parseObject(HttpUtil.postRequestEntity(Const.QUERY_MAJOR, entity));
            Integer errcode = obj.getInteger("errcode");
            if (errcode == 0) {
                JSONArray majorJson = obj.getJSONArray("data");
                Message message=new Message();
                message.what=0;
                message.obj=majorJson.toString();
                handler.sendMessage(message);
            }else{
                handler.sendEmptyMessage(2);
            }
        } catch (Exception e) {
           handler.sendEmptyMessage(2);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
