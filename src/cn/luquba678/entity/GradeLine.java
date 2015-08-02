package cn.luquba678.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.R.string;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GradeLine {
	private Integer year;

	private Integer kelei;

	private Integer aveScore;

	private Integer admitNum, maxScore;

	private String pici;
	
	public String getPici() {
		return pici;
	}

	public void setPici(String pici) {
		this.pici = pici;
	}

	public Integer getYear() {
		return year;
	}

	public String getKeleiName() {
		if (this.kelei == 1) {
			return "理科";
		} else {
			return "文科";
		}
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getKelei() {
		return kelei;
	}

	public void setKelei(Integer kelei) {
		this.kelei = kelei;
	}

	public Integer getAveScore() {
		return aveScore;
	}

	public void setAveScore(Integer aveScore) {
		this.aveScore = aveScore;
	}

	public Integer getAdmitNum() {
		return admitNum;
	}

	public void setAdmitNum(Integer admitNum) {
		this.admitNum = admitNum;
	}

	public Integer getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}
	public static ArrayList<GradeLine> getListFromJson(String jsonData) {
		Type listType = new TypeToken<ArrayList<GradeLine>>() {
		}.getType();
		Gson gson = new Gson();

		ArrayList<GradeLine> queryList = gson.fromJson(jsonData, listType);
		return queryList;
	}

}
