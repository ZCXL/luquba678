package cn.luquba678.entity;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import cn.luquba678.utils.DateUtils;

import com.baidu.navisdk.util.common.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class News {
	// 题目 图 详情链接
	private String title, pic, url;
	// 来源
	private String origin;
	private String headpic;
	private String nickname;
	private int id;
	private String name;
	private String school;
	private String createtime;
	private String content;
	private Integer type;

	public News(int id,Integer type,String pic, String origin,  String createtime,
			String content) {
		super();
		this.pic = pic;
		this.origin = origin;
		this.id = id;
		this.createtime = createtime;
		this.content = content;
		this.type=type;
	}

	public News() {
	}

	public String getHeadpic() {
		if (StringUtils.isEmpty(headpic))
			return null;
		else
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic() {
		if (StringUtils.isEmpty(pic))
			return null;
		else
			return Const.BASE_URL + "/" + pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatetime() {
		if (StringUtils.isNotEmpty(createtime))
			return DateUtils.timeHint(Long.parseLong(createtime) * 1000,
					"yyyy年MM月dd日");
		else
			return "";
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public static ArrayList<News> getListFromJson(String jsonData) {
		Type listType = new TypeToken<ArrayList<News>>() {
		}.getType();
		Gson gson = new Gson();

		ArrayList<News> queryList = gson.fromJson(jsonData, listType);
		return queryList;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

}
