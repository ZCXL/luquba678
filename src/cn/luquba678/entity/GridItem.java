package cn.luquba678.entity;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.luquba678.utils.DateUtils;

public class GridItem{
	int id;
	private String pic;
	private Long create_time;
	private String content;
	private int section = 0;

	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

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
				create_time * 1000);
	}
	public String getTime(String format) {
		return DateUtils.formatDate(format,
				create_time * 1000);
	}

	public void setTime(Long time) {
		this.create_time = time;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}
}
