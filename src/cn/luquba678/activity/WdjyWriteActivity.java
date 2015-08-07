package cn.luquba678.activity;

import cn.luquba678.R;
import cn.luquba678.ui.DialogUtil;
import cn.luquba678.ui.FullScreenDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WdjyWriteActivity extends FullScreenDialog implements OnClickListener, TextWatcher {

	public WdjyWriteActivity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private Button nextButton = null;

	private EditText myWishEditText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wdjy_write_activity);

		myWishEditText = (EditText) findViewById(R.id.et_my_wish);
		myWishEditText.addTextChangedListener(this);

		findViewById(R.id.top_back).setOnClickListener(this);
		((TextView) findViewById(R.id.top_text)).setText("写寄语");

		nextButton = (Button) findViewById(R.id.top_submit);
		nextButton.setText("下一步");
		nextButton.setEnabled(false);
		nextButton.setTextColor(0xFFCFCBCB);
		nextButton.setOnClickListener(this);
	}

	private boolean flag = false;

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() > 0 && !flag) {
			flag = true;
			nextButton.setEnabled(true);
			nextButton.setTextColor(0xFFFFFFFF);
			Log.e("监听内容", "已有输入，可以发布");
		} else if (s.length() == 0 && flag) {
			flag = false;
			nextButton.setEnabled(false);
			nextButton.setTextColor(0xFFCFCBCB);
			Log.e("监听内容", "没有输入，不可以发布");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.dismiss();
			break;
		case R.id.top_submit:
			String strContentString = myWishEditText.getText().toString();
			if (strContentString.isEmpty()) {
				DialogUtil
						.showDialog(activity, "亲，说点什么吧！", false);
				return;
			}
			
			Intent intent = new Intent(activity,
					WdjySaveActivity.class);
			intent.putExtra("MyWish", myWishEditText.getText().toString());
			activity.startActivityForResult(intent, 2);
			break;

		default:
			break;
		}

	}
}
