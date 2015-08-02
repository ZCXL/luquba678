package cn.luquba678.utils;

import java.io.File;
import java.text.Format.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.luquba678.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class EmotionImageGetter {

	public static final String PACKAGE_NAME = "com.alibaba.work.android";
	public static Map<String, Integer> emotionMap = new HashMap<String, Integer>();

	static {
		emotionMap.put("[哈哈]", R.drawable.ic_launcher);
	}

	public static Drawable getEmotionMap(Context context, int resourceId) {
		Drawable drawable = context.getResources().getDrawable(resourceId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		return drawable;
	}

	public static String imgPath(int imgname) {
		String uriStr = "android.resource://" + PACKAGE_NAME + "/" + imgname;

		File imageFile = new File(uriStr);

		return imageFile.getAbsolutePath();
	}

	/*
	 * public String replaceStr(String string){ int indext1
	 * =string.indexOf("["); int indext2 =string.indexOf("]"); String str
	 * =string.substring(indext1, indext2); StringBuffer newStr = new
	 * StringBuffer(); for (int i=0;i<string.length();i++){
	 * if(String.valueOf(string.charAt(i)).equals("[")){ newStr =
	 * newStr.append("<img src=\""); string. break ; }else
	 * if(String.valueOf(string.charAt(i)).equals("]")){ newStr =
	 * newStr.append("\"\" />"); break ; }
	 * newStr=newStr.append(string.charAt(i)); }
	 */

	// "<img src=\""+R.drawable.s001+"\" />";
	public static String encodeImage(String contents) {

		for (Map.Entry<String, Integer> entry : emotionMap.entrySet()) {
			String key = entry.getKey();
			if (contents.indexOf(key) != -1) {
				Pattern pattern = Pattern.compile(Pattern.quote(key));
				Matcher matcher = pattern.matcher(contents);
				contents = matcher.replaceAll(htmlImgString(entry.getValue(),
						key));
			}
		}
		return contents;
	}

	private static String htmlImgString(Integer value, String title) {
		String result = String.format("<img src=\"%s\" />", value);
		return result;
	}

}
