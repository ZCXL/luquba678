package cn.luquba678.entity;

import java.util.ArrayList;
import java.util.List;

public class ResourseAndText {
	private int resourseId;
	private String text;

	public ResourseAndText(int resourseId, String text) {
		this.resourseId = resourseId;
		this.text = text;
	}

	public int getResourseId() {
		return resourseId;
	}

	public void setResourseId(int resourseId) {
		this.resourseId = resourseId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static List<ResourseAndText> getResourseAndTexts() {
		List<ResourseAndText> andTexts = new ArrayList<ResourseAndText>();
		andTexts.add(new ResourseAndText(0, "励志故事"));
		andTexts.add(new ResourseAndText(0, "状元心得"));
		andTexts.add(new ResourseAndText(0, "每日一题"));
		andTexts.add(new ResourseAndText(0, "轻松一刻"));
		andTexts.add(new ResourseAndText(0, "大学排名"));

		return andTexts;
	}
}
