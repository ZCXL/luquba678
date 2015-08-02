package cn.luquba678.entity;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class User {
	public final static String LUQUBA_USER_INFO = "luquba_user_info";
	public final static String PASSWORD = "password";
	public final static String TEL = "tel";
	public final static String LOGIN_TOKEN = "login_token";
	public final static String UID = "uid";
	private static SharedPreferences sharedPreferences;
	/*
	 * {"user":{"uid":"5","username":"u15392955520","email":"",
	 * "tel":"15392955520" ,"headpic":
	 * "Upload\/User\/Headpic\/default.jpg","nickname":"","sex":"1",
	 * "birth":"0000-00-00","province":"","intro":"",
	 * "grade":"0","year":"0","constellation":"","type":"1"},
	 * "login_token":"9ee7d3a97fee6dbe9f41d72f01e51011","expire":"max"}
	 */

	private static Editor editor;

	// "uid":"5","
	private String uid;
	private String sex;
	private String birth;
	private String headpic;
	private String nickname;
	// intro":"",
	private String intro;
	// "grade":"0","
	private String grade;
	// year":"0","
	private String year;

	private String type;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User(String uid, String sex, String birth, String headpic, String nickname, String intro, String grade, String year, String type) {
		super();
		this.uid = uid;
		this.sex = sex;
		this.birth = birth;
		this.headpic = headpic;
		this.nickname = nickname;
		this.intro = intro;
		this.grade = grade;
		this.year = year;
		this.type = type;
	}

	public User() {
	}

	public static Editor getUserEditor(Context context) {
		editor = getUserSharedPreferences(context).edit();// 获取编辑器
		return editor;

	}

	public static SharedPreferences getUserSharedPreferences(Context context) {

		sharedPreferences = context.getSharedPreferences("luquba_login",
				Context.MODE_PRIVATE);
		return sharedPreferences;

	}

	public static String getLoginToken(Context context) {
		return getUserSharedPreferences(context).getString(LOGIN_TOKEN, "");
	}

	public static String getUserString(Context context) {
		return getUserSharedPreferences(context).getString(
				User.LUQUBA_USER_INFO, "");
	}

	public static User getUser(Context context) {

		String userJson = getUserString(context);
		Gson g = new Gson();
		return g.fromJson(userJson, User.class);
	}

	public static String getUID(Context context) {
		return getUserSharedPreferences(context).getString(UID, "");
	}
}
