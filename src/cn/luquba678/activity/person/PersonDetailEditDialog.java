package cn.luquba678.activity.person;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.PersonMessageActivity;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.utils.SPUtils;
import cn.luquba678.utils.ToolUtils;

public class PersonDetailEditDialog extends FullScreenDialog implements
		android.view.View.OnClickListener, TextWatcher {
	private TextView tv_save, tv_detail;
	private EditText et_detail;
	private PersonMessageActivity context;
	private final int intro = 6;
	public PersonDetailEditDialog(PersonMessageActivity context, TextView tv_detail) {
		super(context);
		this.context = context;
		this.tv_detail = tv_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_detail_edit_dialog);
		initTitle(findViewById(R.id.top_back), "编辑个性签名");
		tv_save = (TextView) findViewById(R.id.save);
		if (tv_save.length() > 0) {
			tv_save.setEnabled(true);
			tv_save.setTextColor(0xFFFFFFFF);
		} else {
			tv_save.setEnabled(false);
			tv_save.setTextColor(0xFFCFCBCB);
		}
		tv_save.setOnClickListener(this);
		et_detail = (EditText) findViewById(R.id.et_detail);
		et_detail.setText(tv_detail.getText().toString());
		CharSequence text = et_detail.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
		et_detail.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			ToolUtils.showShortToast(context, "保存成功!!!");
			tv_detail.setText(et_detail.getText().toString());
			context.uploadChange(intro, et_detail.getText()
					.toString());
			dismiss();
		default:
			break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() > 0) {
			tv_save.setEnabled(true);
			tv_save.setTextColor(0xFFFFFFFF);
		} else {
			tv_save.setEnabled(false);
			tv_save.setTextColor(0xFFCFCBCB);
		}
	}

}
