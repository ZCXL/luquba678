package cn.luquba678.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FamousSays {
	// "id":"30","author":"SXS","country":"XazxAz","content":"ZxZx"}}
	private int id;
	private String author;
	private String country;
	private String content;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static FamousSays getListFromJson(String jsonData) {
		Type type = new TypeToken<FamousSays>() {
		}.getType();
		Gson gson = new Gson();
		FamousSays famousSays = gson.fromJson(jsonData, type);
		return famousSays;
	}
}
