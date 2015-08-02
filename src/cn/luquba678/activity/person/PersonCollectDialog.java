package cn.luquba678.activity.person;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.luquba678.R;

public class PersonCollectDialog extends Activity implements OnClickListener {
	private TextView top_text, collect_edit;
	private ImageView top_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_collect);
		initView();
	}

	private void initView() {
		top_text = (TextView) findViewById(R.id.top_text);
		top_text.setText("我的收藏");
		top_back = (ImageView) findViewById(R.id.top_back);
		top_back.setOnClickListener(this);
		collect_edit = (TextView) findViewById(R.id.collect_edit);
		collect_edit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			finish();
			break;
		case R.id.collect_edit:
			break;
		default:
			break;
		}
	}
}
