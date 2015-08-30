package cn.luquba678.activity.fragment;

import cn.luquba678.R;
import cn.luquba678.activity.PersonAccountDialog;
import cn.luquba678.activity.PersonMessageActivity;
import cn.luquba678.activity.person.PersonCollectActivity;
import cn.luquba678.activity.person.PersonSettingDialog;
import cn.luquba678.utils.BitmapUtil;
import cn.luquba678.utils.SPUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuchao.http.Network;
import com.zhuchao.view_rewrite.ExitDialog;

public class PersonalFragment extends Fragment implements OnClickListener {
	PersonAccountDialog account;
	PersonSettingDialog setting;
	View view;
	ImageView head_img;
	TextView name;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_personal, container, false);
		initView();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	void initView() {
		head_img = (ImageView) view.findViewById(R.id.head_img);
		if(Network.checkNetWorkState(getActivity()))
			new DownloadImageTask().execute(SPUtils.get(getActivity(), "headpic", "sss").toString());
		name = (TextView) view.findViewById(R.id.name);
		name.setText(SPUtils.get(getActivity(), "nickname", "ss").toString());
		setClick(R.id.detail_line);
		setClick(R.id.person_account_line);
		setClick(R.id.person_collection);
		setClick(R.id.person_setting);
		setClick(R.id.person_quit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.detail_line:
			startActivityForResult((new Intent(getActivity(), PersonMessageActivity.class)), 6);
			break;
		case R.id.person_account_line:
			account = new PersonAccountDialog(getActivity());
			account.show();
			break;
		case R.id.person_collection:
			startActivity(new Intent(getActivity(), PersonCollectActivity.class));
			break;
		case R.id.person_setting:
			setting = new PersonSettingDialog(getActivity());
			setting.show();
			break;
		case R.id.person_quit:
			new ExitDialog(getActivity()).show();
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 6) {
			if(Network.checkNetWorkState(getActivity()))
				new DownloadImageTask().execute(SPUtils.get(getActivity(), "headpic", "sss").toString());
			name.setText(SPUtils.get(getActivity(), "nickname", "ss").toString());
			HomeFragment.setYear((String)SPUtils.get(getActivity(),"year","2016"));
		}
	}

	private void setClick(int Id) {
		view.findViewById(Id).setOnClickListener(this);
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Drawable>   
	{  
	          
	        protected Drawable doInBackground(String... urls) {  
	            return BitmapUtil.loadImageFromNetwork(urls[0]);  
	        }  
	  
	        protected void onPostExecute(Drawable result) {  
	        	head_img.setImageDrawable(result);  
	        }  
	} 
}
