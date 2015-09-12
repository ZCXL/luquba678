package cn.luquba678.activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import cn.luquba678.R;
import cn.luquba678.ui.FullScreenDialog;

public class AgreementDialog extends FullScreenDialog implements android.view.View.OnClickListener {

	private LinearLayout back;
	public AgreementDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_dialog);
		back=(LinearLayout)findViewById(R.id.top_back);
		back.setOnClickListener(this);
		WebView wv= getView(R.id.content);
		wv.loadUrl("file:///android_asset/arguement.html");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.top_back:
				this.dismiss();
				break;
			default:
				break;
		}
	}
}
