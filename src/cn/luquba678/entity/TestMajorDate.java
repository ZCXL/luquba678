package cn.luquba678.entity;

import java.util.ArrayList;

public class TestMajorDate {

	public String majorName;
	public String majorContent;

	public TestMajorDate(String majorName, String majorContent) {
		this.majorName = majorName;
		this.majorContent = majorContent;
	}

	public TestMajorDate() {
	}

	public static ArrayList<TestMajorDate> getDates() {
		ArrayList<TestMajorDate> tmds = new ArrayList<TestMajorDate>();
		TestMajorDate tmd = new TestMajorDate(
				"计算机科学与技术(软件方向)",
				"从此在网上看电影不要钱哟！！(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡从此在网上看电影不要钱哟！！(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡从此在网上看电影不要钱哟！！(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡从此在网上看电影不要钱哟！！(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡从此在网上看电影不要钱哟！！(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡(๑╹ڡ╹)╭ ～ ♡");
		tmds.add(tmd);
		tmds.add(new TestMajorDate(
				"应用心理学",
				"最近的电影真好看！！！ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)最近的电影真好看！！！ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)最近的电影真好看！！！ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)最近的电影真好看！！！ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)最近的电影真好看！！！ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)　ლ(^o^ლ)"));
		return tmds;
	}

}
