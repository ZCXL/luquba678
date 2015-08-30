package cn.luquba678.activity.person;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.PersonMessageActivity;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.utils.SPUtils;
import cn.luquba678.utils.ToolUtils;

public class PersonDetailEditDialog extends FullScreenDialog implements android.view.View.OnClickListener, TextWatcher {
	private TextView tv_save, tv_detail;
	private EditText et_detail;
	private PersonMessageActivity context;
	private final int intro = 6;
	private ImageView back;
	private RelativeLayout back_text;
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
		et_detail.setText(SPUtils.get(context, "intro", "***").toString());
		CharSequence text = et_detail.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
		et_detail.addTextChangedListener(this);

		back=(ImageView)findViewById(R.id.title_top_image);
		back_text=(RelativeLayout)findViewById(R.id.back_text);
		back_text.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.save:
				String content=et_detail.getText().toString();
				if(content.length()>8)
					content=content.substring(0,8)+"...";
				tv_detail.setText(content);
				context.uploadChange(intro, et_detail.getText().toString());
				dismiss();
			case R.id.title_top_image:
				dismiss();
				break;
			case R.id.back_text:
				dismiss();
				break;
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
