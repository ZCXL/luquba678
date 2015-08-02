package cn.luquba678.activity.person;

import android.content.Context;
import android.os.Bundle;
import cn.luquba678.R;
import cn.luquba678.ui.FullScreenDialog;

public class PersonQuitDialog extends FullScreenDialog {

	public PersonQuitDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// initTitle(findViewById(R.id.person_quit_title), "登陆管理");
	}
}
