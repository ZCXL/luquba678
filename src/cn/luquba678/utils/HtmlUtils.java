package cn.luquba678.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.Html.ImageGetter;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HtmlUtils {

	private static Context mContext;

	private static Pattern atPattern = Pattern
			.compile("@[0-9a-zA-Z\u4e00-\u9fa5\\s]+\\([0-9a-zA-Z_]+\\)");

	/**
	 * textview中的文本需要转化为链接，表情，同时textview所在的view group在list view中，需要触发on item
	 * click事件 主墙表情
	 * 
	 * @param tv
	 * @param vg
	 * @param postId
	 */
	public static void transHtmlWithItemClick(final TextView tv,
			final Context context) {

		// 获取文本内容
		String content = tv.getText().toString();

		mContext = context;

		// 使用正则匹配@ html等 转化为链接
		content = replaceHtmlLink(content);
		String replaceFacebook = EmotionImageGetter.encodeImage(content);
		// 重设textview值
		final Html.ImageGetter imageGetter = getImageGetter(context);
		// 重设textview值
		tv.setText(Html.fromHtml(replaceFacebook, imageGetter, null));

		// 监控listview中的文本点击以及Listview中的item点击事件
		tv.setMovementMethod(LinkClickTextView.LocalLinkMovementMethod
				.getInstance());
		tv.setLongClickable(false);
		// @ html等链接处理
		addTextViewLink(tv);
	}

	public static void addTextViewLink(final TextView tv) {
		CharSequence text = tv.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) tv.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			ImageSpan[] images = sp.getSpans(0, end, ImageSpan.class);

			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();// should clear old spans

			// 循环把链接发过去
			for (URLSpan url : urls) {
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL(), tv);
				style.setSpan(myURLSpan, sp.getSpanStart(url),
						sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}

			for (ImageSpan image : images) {
				Drawable drawable = EmotionImageGetter.getEmotionMap(
						tv.getContext(), Integer.parseInt(image.getSource()));
				style.setSpan(new ImageSpan(drawable), sp.getSpanStart(image),
						sp.getSpanEnd(image),
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}

			tv.setText(style);
		}
	}

	private static ImageGetter getImageGetter(final Context context) {
		return new Html.ImageGetter() {
			public Drawable getDrawable(String source) {
				Drawable drawable = EmotionImageGetter.getEmotionMap(context,
						Integer.parseInt(source));
				return drawable;
			};
		};
	}

	public static String replaceHtmlLink(String content) {
		List<String> list = new LinkedList<String>();

		Matcher matcher = atPattern.matcher(content);

		while (null != matcher && matcher.find()) {
			if (!list.contains(matcher.group())) {
				content = content.replace(matcher.group(), "<a href=\""
						+ matcher.group() + "\">" + matcher.group() + "</a>");
			}
			list.add(matcher.group());
		}
		return content;
	}

	public static class MyURLSpan extends ClickableSpan {
		private String mUrl;

		private TextView tv;

		public MyURLSpan(String url, TextView tv) {
			mUrl = url;
			this.tv = tv;
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(tv.getContext().getResources()
					.getColor(android.R.color.background_dark));
			ds.setUnderlineText(false);
		}

		@Override
		public void onClick(View widget) {
			if (mUrl.startsWith("@")) {
				Toast.makeText(mContext, mUrl.replaceFirst("@", ""),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
