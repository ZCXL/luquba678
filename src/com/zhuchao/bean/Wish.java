package com.zhuchao.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.navisdk.util.common.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.luquba678.utils.DateUtils;

/**
 * Created by zhuchao on 8/12/15.
 */
public class Wish implements Parcelable{
    private String id;
    private String pic;
    private String content;
    private String create_time;

    public Wish(String id, String pic, String content, String create_time) {
        this.id = id;
        this.pic = pic;
        this.content = content;
        this.create_time = create_time;
    }
    public Wish(String result){
        try {
            JSONObject object=new JSONObject(result);
            setId(object.getString("id"));
            setPic(object.getString("pic"));
            setCreate_time(object.getString("createtime"));
            setContent(object.getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pic);
        dest.writeString(content);
        dest.writeString(create_time);
    }
    public static final Parcelable.Creator<Wish>CREATOR=new Creator<Wish>() {
        @Override
        public Wish createFromParcel(Parcel source) {
            return new Wish(source);
        }

        @Override
        public Wish[] newArray(int size) {
            return new Wish[size];
        }
    };
    private Wish(Parcel dest){
        id=dest.readString();
        pic=dest.readString();
        content=dest.readString();
        create_time=dest.readString();
    }
}
