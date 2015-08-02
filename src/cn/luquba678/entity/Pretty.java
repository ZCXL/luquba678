package cn.luquba678.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Pretty {
	private int id;
	private String name;
	private String school;
	private String pic;
	private String url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static ArrayList<Pretty> getListFromJson(String jsonData) {
		Type listType = new TypeToken<ArrayList<Pretty>>() {
		}.getType();
		Gson gson = new Gson();
		ArrayList<Pretty> queryList = gson.fromJson(jsonData, listType);
		return queryList;
	}
}
