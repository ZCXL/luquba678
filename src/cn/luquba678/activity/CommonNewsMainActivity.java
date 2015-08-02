package cn.luquba678.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.luquba678.R;
import cn.luquba678.activity.adapter.StoryAdapter;
import cn.luquba678.entity.News;

public class CommonNewsMainActivity extends Activity implements
		OnClickListener, OnItemClickListener {

	private ListView listview_stories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_detail_news_page);
		findViewById(R.id.back_button).setOnClickListener(this);
		((TextView) findViewById(R.id.story_title)).setText(getIntent()
				.getStringExtra("title"));
		// ObjectAnimator.ofFloat(listview_stories,
		// "x",0f,200f).setDuration(1000l).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			this.finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TextView title = (TextView) arg1.findViewById(R.id.story_title);
		Toast.makeText(this, title.getText(), 0).show();
		Intent intent = new Intent(this, CommonNewsMainActivity.class);
		intent.putExtra("title", title.getText());
		startActivity(intent);

	}
}
