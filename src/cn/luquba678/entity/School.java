package cn.luquba678.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class School {
	public static final String GRADE = "grade";
	public static final String HOME_AREA_ID = "home_area_id";
	public static final String KELEI = "kelei";
	public static final String SCHOOL_AREA_ID = "school_area_id";
	// school_id：学校编号
	private Integer school_id;
	// school_name：学校名称;
	String school_name;
	// area_id：学校所在地区编号
	private Integer area_id;
	// pici：批次
	private String pici;
	// is_211：是否是211
	private Integer is_211;
	// is_985：是否是985
	private Integer is_985;
	private Integer is_Yanjiusheng;
	// logo：学校logo
	private String logo;
	private String type;
	private String tel;
	private String web;
	private String gradeline;
	private String year;
	private Integer maxScore;
	private Integer rank;
	private Integer admitNum;
	private String intro;

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getGradeline() {
		return gradeline;
	}

	public void setGradeline(String gradeline) {
		this.gradeline = gradeline;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Integer getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}

	public Integer getAdmitNum() {
		return admitNum;
	}

	public void setAdmitNum(Integer admitNum) {
		this.admitNum = admitNum;
	}

	public Integer getIs_Yanjiusheng() {
		return is_Yanjiusheng;
	}

	public void setIs_Yanjiusheng(Integer is_Yanjiusheng) {
		this.is_Yanjiusheng = is_Yanjiusheng;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public Integer getArea_id() {
		return area_id;
	}

	public void setArea_id(Integer area_id) {
		this.area_id = area_id;
	}

	public String getPici() {
		return pici;
	}

	public void setPici(String pici) {
		this.pici = pici;
	}

	public Integer getIs_211() {
		return is_211;
	}

	public void setIs_211(Integer is_211) {
		this.is_211 = is_211;
	}

	public Integer getIs_985() {
		return is_985;
	}

	public void setIs_985(Integer is_985) {
		this.is_985 = is_985;
	}

	public String getLogo() {
		return Const.BASE_URL + "/" + logo;
	}

	public String getAreaName() {
		return CityMsg.getShortNameFromId(area_id).area_name;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public School() {
	}

	public static ArrayList<School> getListFromJson(String jsonData) {
		Type listType = new TypeToken<ArrayList<School>>() {
		}.getType();
		Gson gson = new Gson();

		ArrayList<School> queryList = gson.fromJson(jsonData, listType);
		return queryList;
	}

	public static School getSchoolFromJson(String jsonData) {
		Gson gson = new Gson();
		School queryList = gson.fromJson(jsonData, School.class);
		return queryList;
	}
}
