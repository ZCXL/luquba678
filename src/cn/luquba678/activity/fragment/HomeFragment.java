package cn.luquba678.activity.fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.luquba678.R;
import cn.luquba678.activity.CommonNewsActivity;
import cn.luquba678.activity.FunnyActivity;
import cn.luquba678.activity.PrettySchoolMateActivity;
import cn.luquba678.activity.SubMainActivity;
import cn.luquba678.activity.MajorsDetailActivity;
import cn.luquba678.activity.UniversityListActivity;
import cn.luquba678.entity.ResourseAndText;
import cn.luquba678.utils.DateUtils;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener,
		OnCheckedChangeListener {

	private TextView count;
	private String year;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.home_main_fragment, container, false);
	}

	public View findViewById(int id) {
		return this.getView().findViewById(id);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ste = Executors.newSingleThreadExecutor();
		findViewById(R.id.top_back).setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.top_text)).setText("录取吧");
		// 到顶部
		count = (TextView) findViewById(R.id.count);
		// year = DateUtils.formatDate("yyyy", System.currentTimeMillis());
		year = "2016";
		// updateHandler.post(updateThread);
		findViewById(R.id.home_champion_experience).setOnClickListener(this);
		findViewById(R.id.home_funny).setOnClickListener(this);
		findViewById(R.id.home_pretty_school_mate).setOnClickListener(this);
		findViewById(R.id.home_school_rank).setOnClickListener(this);
		findViewById(R.id.home_story).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.home_champion_experience:
			title = "状元心得";
			break;
		case R.id.home_funny:
			intent = new Intent(getActivity(), FunnyActivity.class);
			getActivity().startActivity(intent);
			return;
		case R.id.home_pretty_school_mate:
			intent = new Intent(getActivity(), PrettySchoolMateActivity.class);
			getActivity().startActivity(intent);
			return;
		case R.id.home_school_rank:
			title = "学校排名";
			intent = new Intent(getActivity(), UniversityListActivity.class);
			getActivity().startActivity(intent);
			return;
		case R.id.home_story:
			title = "励志故事";
			break;
		default:
			break;
		}
		intent = new Intent(getActivity(), SubMainActivity.class);
		intent.putExtra("title", title);
		getActivity().startActivity(intent);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
	}

	private boolean countFlag = true;

	Runnable updateThread = new Runnable() {

		@Override
		public void run() {
			// 获取消息
			Message msg = updateHandler.obtainMessage();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			msg.obj = DateUtils
					.countNationalHigherEducationEntranceExamination(year);
			// 把此消息发送到消息队列中
			if (countFlag) {

				updateHandler.sendMessage(msg);
			}
		}
	};

	Handler updateHandler = new Handler() {
		// 把消息从消息队列中取出处理
		public void handleMessage(Message msg) {
			count.setText(msg.obj.toString());
			ste.execute(updateThread);
		}
	};
	private static ExecutorService ste;

	@Override
	public void onPause() {
		countFlag = false;
		super.onPause();
	}

	@Override
	public void onStop() {
		countFlag = false;
		super.onStop();
	}

	@Override
	public void onResume() {
		count.setText(DateUtils
				.countNationalHigherEducationEntranceExamination(year));
		ste.execute(updateThread);
		super.onResume();
	}

	@Override
	public void onStart() {
		countFlag = true;
		// 开始倒计时
		super.onStart();
	}

}
