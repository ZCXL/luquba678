package com.zhuchao.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhuchao on 8/21/15.
 */
public class Advertisement implements Parcelable{
    private String id;
    private String pic;
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Advertisement(String id, String pic, String url) {
        this.id = id;
        this.pic = pic;
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pic);
        dest.writeString(url);
    }
    public static final Creator<Advertisement>CREATOR=new Creator<Advertisement>() {
        @Override
        public Advertisement createFromParcel(Parcel source) {
            return new Advertisement(source);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }
    };
    private Advertisement(Parcel source){
        id=source.readString();
        pic=source.readString();
        url=source.readString();
    }
    public Advertisement(String result){
        try {
            JSONObject object=new JSONObject(result);
            setId(object.getString("id"));
            setPic(object.getString("pic"));
            setUrl(object.getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
