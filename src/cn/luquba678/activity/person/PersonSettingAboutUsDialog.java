package cn.luquba678.activity.person;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.ui.FullScreenDialog;

public class PersonSettingAboutUsDialog extends FullScreenDialog {

	private Context context;
	private TextView about_tv;

	public PersonSettingAboutUsDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_setting_about_us);
		initTitle(findViewById(R.id.person_setting_title), "关于我们");
		about_tv = (TextView) findViewById(R.id.about_tv);
		String tv_about_us = "    "
				+ context.getResources().getString(R.string.about_us_one)
				+ "    "
				+ context.getResources().getString(R.string.about_us_two);
		about_tv.setText(tv_about_us);
	}

}
