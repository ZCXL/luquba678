package cn.luquba678.activity.person;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.PersonMessageActivity;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.utils.ToolUtils;

public class PersonNameEditDialog extends FullScreenDialog implements android.view.View.OnClickListener, TextWatcher {
	private TextView tv_save, tv_name, tv_count;
	private EditText et_name;
	private PersonMessageActivity context;
	private final int nick_name = 2;
	private ImageView back_image;
    private RelativeLayout back;
	public PersonNameEditDialog(PersonMessageActivity context, TextView tv_name) {
		super(context);
		this.context = context;
		this.tv_name = tv_name;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_name_edit_dialog);
		tv_save = (TextView) findViewById(R.id.save);
		tv_count = (TextView) findViewById(R.id.tv_count);
		int tv_length = tv_name.length();
		int maxInputLength = 8;
		int showLenth = maxInputLength - tv_length;
		if (tv_length > 0) {
			if (showLenth > 0) {
				tv_count.setTextColor(context.getResources().getColor(
						R.color.black));
			} else {
				tv_count.setTextColor(context.getResources().getColor(
						R.color.red));
			}
		}
		tv_count.setText(showLenth + "");
		tv_save.setOnClickListener(this);
		et_name = (EditText) findViewById(R.id.et_name);
		et_name.setText(tv_name.getText().toString());
		CharSequence text = et_name.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
		et_name.addTextChangedListener(this);

		back_image=(ImageView)findViewById(R.id.title_top_image);
        back_image.setOnClickListener(this);
        back=(RelativeLayout)findViewById(R.id.top_back);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.save:
                String name = et_name.getText().toString();
                if (checkIsCanSaveName(name)) {
                    ToolUtils.showShortToast(context, "保存成功!!!");
                    tv_name.setText(et_name.getText().toString());
                    context.uploadChange(nick_name, name);
                    dismiss();
                }
                break;
            case R.id.title_top_image:
                dismiss();
                break;
            case R.id.top_back:
                dismiss();
                break;
            default:
                break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		int tv_length = s.length();
		int maxInputLength = 8;
		int showLength = maxInputLength - tv_length;
		if (tv_length > 0) {
			if (showLength > 0) {
				tv_count.setTextColor(context.getResources().getColor(
						R.color.black));
			} else {
				tv_count.setTextColor(context.getResources().getColor(
						R.color.red));
			}
		} else {
			tv_count.setTextColor(context.getResources()
					.getColor(R.color.black));
		}
		tv_count.setText(showLength + "");
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		// KeyBoardUtils.closeKeybord(et_name, context);
		if (!(getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)) {
			Log.i("wyb", "onDetachedFromWindow");
		}
	}

	private boolean checkIsCanSaveName(String checkName) {
		if (checkName.length() == 0) {
			ToolUtils.showShortToast(context, "内容不能为空");
			return false;
		} else if (checkName.length() > 8) {
			ToolUtils.showShortToast(context, "内容不能超出字数限制");
			return false;
		}
		return true;
	}

}
