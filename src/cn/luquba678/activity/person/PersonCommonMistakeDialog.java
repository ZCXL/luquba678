package cn.luquba678.activity.person;

import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.PersonMessageActivity;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.ui.HttpUtil;

public class PersonCommonMistakeDialog extends FullScreenDialog {

	private TextView common_mistake;
	private Context context;
	public PersonCommonMistakeDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_common_mistake);
		initTitle(findViewById(R.id.person_setting_title), "常见问题");
		common_mistake = (TextView) findViewById(R.id.common_mistake);
		common_mistake.setText("");
		loadCommonMistake();
	}

	private void loadCommonMistake() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					String get_help_url = Const.GET_HELP+"?uid="+User.getUID(context)
							+"&login_token="+User.getLoginToken(context);
					JSONObject obj = HttpUtil.getRequestJson(get_help_url, null);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
}
