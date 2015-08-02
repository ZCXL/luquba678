package cn.luquba678.activity.person;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import cn.luquba678.R;
import cn.luquba678.activity.CommonActivity;
import cn.luquba678.ui.FullScreenDialog;

public class PersonMessageDialog extends FullScreenDialog {

	public PersonMessageDialog(Activity context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_detail);
		initTitle(findViewById(R.id.person_detail_title), "个人信息");
	}

}
