package cn.luquba678.entity;

import java.util.ArrayList;

public class People {
	private String imageUrl;
	private String name;
	private String introduce;
	private ArrayList<String> images;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public People(String imageUrl, String name, String introduce,
			ArrayList<String> images) {
		super();
		this.imageUrl = imageUrl;
		this.name = name;
		this.introduce = introduce;
		this.images = images;
	}

	public People(String imageUrl, String name, String introduce) {
		super();
		this.imageUrl = imageUrl;
		this.name = name;
		this.introduce = introduce;
	}

	public People() {
		super();
	}

}
