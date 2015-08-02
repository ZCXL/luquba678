package cn.luquba678.activity.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class TextLenthWatcher implements TextWatcher {

	private boolean flag = false;
	private Button nextButton;
	private int length;

	public TextLenthWatcher(Button nextButton, int length) {
		this.nextButton = nextButton;
		this.length = length;
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() >= length && !flag) {
			flag = true;
			nextButton.setEnabled(true);
			nextButton.setTextColor(0xFFFFFFFF);
		} else if (s.length() < length && flag) {
			flag = false;
			nextButton.setEnabled(false);
			nextButton.setTextColor(0xFFCFCBCB);
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

}
