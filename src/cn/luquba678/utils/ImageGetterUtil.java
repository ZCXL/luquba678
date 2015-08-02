package cn.luquba678.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

public class ImageGetterUtil implements Html.ImageGetter {
	private Context c;

	public ImageGetterUtil(Context c) {
		this.c = c;
	}

	@Override
	public Drawable getDrawable(String source) {
		return c.getResources().getDrawable(Integer.parseInt(source));
	}
}
