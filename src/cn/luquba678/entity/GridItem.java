package cn.luquba678.entity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.luquba678.utils.DateUtils;

public class GridItem{
	int id;
	private String pic;
	private Long createtime;
	private String content;
	private int section = 0;



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}
	public static ArrayList<GridItem> getListFromJson(String jsonData) {
		Type listType = new TypeToken<ArrayList<GridItem>>() {
		}.getType();
		Gson gson = new Gson();
		ArrayList<GridItem> queryList = gson.fromJson(jsonData, listType);
		return queryList;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPath() {
		return Const.BASE_URL + "/" + pic;
	}

	public void setPath(String pic) {
		this.pic = pic;
	}

	public String getTime() {
		return DateUtils.formatDate("yyyy年MM月",
				createtime * 1000);
	}
	public String getTime(String fomat) {
		return DateUtils.formatDate(fomat,
				createtime * 1000);
	}

	public void setTime(Long time) {
		this.createtime = time;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}
}
