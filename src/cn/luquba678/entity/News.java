package cn.luquba678.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import cn.luquba678.utils.DateUtils;

import com.baidu.navisdk.util.common.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class News implements Parcelable{
	// 题目 图 详情链接
	private String title, pic, url;
	// 来源
	private String origin;
	private String id;
	private String createtime;
	private String intro;
	private String author;
	private String content;

	public News(String id,String pic, String origin,String createtime,String title,String intro,String author,String url,String content) {
		super();
		this.id = id;
		this.title=title;
		this.pic = pic;
		this.origin = origin;
		this.intro=intro;
		this.author=author;
		this.url=url;
		this.createtime = createtime;
		this.content=content;
	}

	public News() {

	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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
			return pic;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static ArrayList<News> getListFromJson(String jsonData) {
		Type listType = new TypeToken<ArrayList<News>>() {}.getType();
		Gson gson = new Gson();

		ArrayList<News> queryList = gson.fromJson(jsonData, listType);
		return queryList;
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(pic);
        dest.writeString(url);
        dest.writeString(origin);
        dest.writeString(createtime);
        dest.writeString(intro);
        dest.writeString(author);
		dest.writeString(content);
    }
    public static  final Parcelable.Creator<News>CREATOR=new Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
    protected News(Parcel in) {
        id=in.readString();
        title=in.readString();
        pic=in.readString();
        url=in.readString();
        origin=in.readString();
        createtime=in.readString();
        intro=in.readString();
        author=in.readString();
		content=in.readString();
    }
}
