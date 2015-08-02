package cn.luquba678.activity;

import android.content.Context;
import android.os.Bundle;
import cn.luquba678.R;
import cn.luquba678.ui.FullScreenDialog;

public class PersonAccoutDialog extends FullScreenDialog {

	public PersonAccoutDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_account);
		initTitle(findViewById(R.id.person_accout_title), "我的账号");
	}

}
