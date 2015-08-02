package cn.luquba678.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author Collin
 *
 */
public class MatriculateMsg {

	// major_name：专业名称，如：建筑学
	private String major_name;
	private ArrayList<GradeLine> gradeline;

	public String getMajor_name() {
		return major_name;
	}

	public void setMajor_name(String major_name) {
		this.major_name = major_name;
	}

	public ArrayList<GradeLine> getGradeline() {
		return gradeline;
	}

	public void setGradeline(ArrayList<GradeLine> gradeline) {
		this.gradeline = gradeline;
	}

	public static ArrayList<MatriculateMsg> getListFromJson(String jsonData) {
		Type listType = new TypeToken<ArrayList<MatriculateMsg>>() {
		}.getType();
		Gson gson = new Gson();

		ArrayList<MatriculateMsg> queryList = gson.fromJson(jsonData, listType);
		return queryList;
	}

}