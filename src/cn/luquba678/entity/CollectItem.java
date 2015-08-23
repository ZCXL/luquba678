package cn.luquba678.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.navisdk.util.common.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.luquba678.utils.DateUtils;

public class CollectItem extends News{
	private String type;
	private String collect_time;
	public CollectItem(String id,String pic, String origin,String create_time,String title,String intro,String author,String url,String content,String type,String collect_time){
		super(id,pic,origin,create_time,title,intro,author,url,content);
		this.type=type;
		this.collect_time=collect_time;
	}

	public CollectItem(String result){
		try {
			JSONObject jsonObject=new JSONObject(result);
			setId(jsonObject.getString("id"));
			setUrl(jsonObject.getString("url"));
			setCreatetime(jsonObject.getString("createtime"));
			setCollect_time(jsonObject.getString("collect_createtime"));
			setType(jsonObject.getString("type"));
			setAuthor(jsonObject.getString("author"));
			setPic(jsonObject.getString("pic"));
			setTitle(jsonObject.getString("title"));
			setOrigin(jsonObject.getString("origin"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCollect_time() {
        if (StringUtils.isNotEmpty(collect_time))
            return DateUtils.timeHint(Long.parseLong(collect_time) * 1000,
                    "yyyy年MM月dd日");
        else
            return "";
	}

	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest,flags);
		dest.writeString(type);
		dest.writeString(collect_time);
	}
	public static  final Parcelable.Creator<News>CREATOR=new Creator<News>() {
		@Override
		public CollectItem createFromParcel(Parcel source) {
			return new CollectItem(source);
		}

		@Override
		public CollectItem[] newArray(int size) {
			return new CollectItem[size];
		}
	};
	private CollectItem(Parcel in) {
		super(in);
		type=in.readString();
		collect_time=in.readString();
	}
}
