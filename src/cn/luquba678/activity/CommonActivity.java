package cn.luquba678.activity;

import cn.luquba678.R;
import cn.luquba678.utils.ImageLoader;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public abstract class CommonActivity extends Activity implements
		OnClickListener {
	public final static int CHANGE = 0, ADD = 1;
	ProgressDialog dialog;
	public final Activity self = this;
	private View textView;
	ImageLoader imageLoader;
	public <T extends View> T getView(int viewId) {
		return (T) findViewById(viewId);
	}
public ImageLoader getImageLoader(){
	imageLoader = new ImageLoader(self);
	return imageLoader;
}
	public void setOnClickLinstener(int... ids) {
		for (int i = 0; i < ids.length; i++) {
			this.findViewById(ids[i]).setOnClickListener(this);
		}
	}

	public void toast(String str) {
		Toast.makeText(self, str, Toast.LENGTH_SHORT).show();
	}

}
