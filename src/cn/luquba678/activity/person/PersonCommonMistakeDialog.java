package cn.luquba678.activity.person;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuchao.adapter.QAAdpter;
import com.zhuchao.bean.QA;
import com.zhuchao.http.Network;
import com.zhuchao.view_rewrite.LoadingDialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.ui.HttpUtil;

public class PersonCommonMistakeDialog extends FullScreenDialog {
	private Context context;
	private LinearLayout back;
	private ImageView back_image;
	private ListView listView;
	private QAAdpter adapter;
	private ArrayList<QA>qas;
	private LoadingDialog loadingDialog;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					adapter.notifyDataSetChanged();
					break;
			}
			loadingDialog.stopProgressDialog();
		}
	};
	public PersonCommonMistakeDialog(Context context) {
		super(context);
		this.context = context;
		loadingDialog=new LoadingDialog(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_common_mistake);
		back=(LinearLayout)findViewById(R.id.top_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		back_image=(ImageView)findViewById(R.id.title_top_image);
		back_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		listView=(ListView)findViewById(R.id.common_mistake);
		qas=new ArrayList<QA>();
		adapter=new QAAdpter(qas,context);
		listView.setAdapter(adapter);
		loadCommonMistake();
	}

	private void loadCommonMistake() {
		if(Network.checkNetWorkState(context))
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					try {
						String get_help_url = Const.GET_HELP+"?uid="+User.getUID(context)+"&login_token="+User.getLoginToken(context);
						JSONObject obj = HttpUtil.getRequestJson(get_help_url, null);
						int err_code=obj.getIntValue("errcode");
						if(err_code==0){
							JSONArray array=obj.getJSONArray("data");
							for(int i=0;i<array.size();i++){
								QA qa=new QA(array.getString(i));
								qas.add(qa);
							}
							handler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(0);
						e.printStackTrace();
					}
				}
			});
	}
}
