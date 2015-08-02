package cn.luquba678.activity;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.luquba678.R;

public class BirthdayActivity extends Activity implements
		OnWheelChangedListener, OnClickListener {
	private WheelView mYear;
	private WheelView mMonth;
	private WheelView mDay;
	private TextView showChooseOK, showChooseCancle,choose_title;
	private String[] mYearDatas;
	private String[] mMonthDatas;
	private String[] mDayDatas;
	public String mCurrentYear;
	public String mCurrentMonth;
	public String mCurrentDay = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose);
		mYear = (WheelView) findViewById(R.id.id_province);
		mMonth = (WheelView) findViewById(R.id.id_city);
		mDay = (WheelView) findViewById(R.id.id_area);
		showChooseOK = (TextView) findViewById(R.id.showChooseOK);
		showChooseOK.setOnClickListener(this);
		showChooseCancle = (TextView) findViewById(R.id.showChooseCancle);
		showChooseCancle.setOnClickListener(this);
		choose_title = (TextView) findViewById(R.id.choose_title);
		choose_title.setText("请选择出生日期");
		initDatas();
		updateYear();
		updateMonth();
		updateDay();
		mYear.addChangingListener(this);
		mMonth.addChangingListener(this);
		mDay.addChangingListener(this);
		mYear.setVisibleItems(5);
		mMonth.setVisibleItems(5);
		mDay.setVisibleItems(5);
	}

	private void updateDay() {
		int pCurrent = mDay.getCurrentItem();
		mCurrentDay = mDayDatas[pCurrent];
		mDay.setViewAdapter(new ArrayWheelAdapter<String>(this, mDayDatas));
		mDay.setCurrentItem(pCurrent);
	}

	private void updateMonth() {
		int pCurrent = mMonth.getCurrentItem();
		mCurrentMonth = mMonthDatas[pCurrent];
		mMonth.setViewAdapter(new ArrayWheelAdapter<String>(this, mMonthDatas));
		mMonth.setCurrentItem(pCurrent);
	}

	private void updateYear() {
		int pCurrent = mYear.getCurrentItem();
		mCurrentYear = mYearDatas[pCurrent];
		mYear.setViewAdapter(new ArrayWheelAdapter<String>(this, mYearDatas));
		mYear.setCurrentItem(pCurrent);
	}

	private void initDatas() {
		mYearDatas = new String[36];
		for (int i = 0; i < mYearDatas.length; i++) {
			mYearDatas[i] = 1970 + i + "";
		}
		mMonthDatas = new String[12];
		for (int j = 0; j < mMonthDatas.length; j++) {
			mMonthDatas[j] = j + 1 + "";
		}
		mDayDatas = new String[31];
		for (int k = 0; k < mDayDatas.length; k++) {
			mDayDatas[k] = k + 1 + "";
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		updateDay();
		updateMonth();
		updateYear();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.showChooseOK:
			Intent intent = new Intent();
			intent.putExtra("year", mCurrentYear);
			intent.putExtra("month", mCurrentMonth);
			intent.putExtra("day", mCurrentDay);
			setResult(6, intent);
			finish();
			break;
		case R.id.showChooseCancle:
			finish();
			break;
		default:
			break;
		}

	}
}
