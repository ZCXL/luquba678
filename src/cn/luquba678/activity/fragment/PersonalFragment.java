package cn.luquba678.activity.fragment;

import cn.luquba678.R;
import cn.luquba678.activity.LoginActivity;
import cn.luquba678.activity.PersonAccountDialog;
import cn.luquba678.activity.PersonMessageActivity;
import cn.luquba678.activity.person.PersonCollectActivity;
import cn.luquba678.activity.person.PersonSettingDialog;
import cn.luquba678.utils.BitmapUtil;
import cn.luquba678.utils.SPUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalFragment extends Fragment implements OnClickListener {
	PersonAccountDialog account;
	PersonSettingDialog setting;
	View view;
	ImageView head_img;
	TextView name, detail;

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

	private View findViewById(int id) {
		return getView().findViewById(id);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findViewById(R.id.top_back).setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.top_text)).setText("个人");
		this.getView().findViewById(R.id.editer_top2).setOnClickListener(this);
	}

	void initView() {
		head_img = (ImageView) view.findViewById(R.id.head_img);
		new DownloadImageTask().execute(SPUtils.get(getActivity(), "headpic", "sss").toString());
		name = (TextView) view.findViewById(R.id.name);
		name.setText(SPUtils.get(getActivity(), "nickname", "ss").toString());
		detail = (TextView) view.findViewById(R.id.detail);
		detail.setText(SPUtils.get(getActivity(), "intro", "***").toString());
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
			showExitDialog();
			break;
		case R.id.btn_cancle:
			exitDialog.dismiss();
			break;
		case R.id.btn_ok:
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			loginOut();
			startActivity(intent);
			exitDialog.dismiss();
			getActivity().finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 6) {
			name.setText(SPUtils.get(getActivity(), "nickname", "ss").toString());
			detail.setText(SPUtils.get(getActivity(), "intro", "***").toString());
			new DownloadImageTask().execute(SPUtils.get(getActivity(), "headpic", "sss").toString());
			HomeFragment.setYear((String)SPUtils.get(getActivity(),"year","2016"));
		}
	}

	private void setClick(int Id) {
		view.findViewById(Id).setOnClickListener(this);

	}


	AlertDialog exitDialog;

	private void showExitDialog() {
		exitDialog = new AlertDialog.Builder(getActivity()).create();
		exitDialog.show();
		exitDialog.getWindow().setContentView(R.layout.person_exit_dialog);
		Button btn_cancle, btn_ok;
		btn_cancle = (Button) exitDialog.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(this);
		btn_ok = (Button) exitDialog.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
	}

	public void loginOut() {
		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("luquba_login", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putString("login_token", "");
		editor.putString("uid", "");
		editor.commit();
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
