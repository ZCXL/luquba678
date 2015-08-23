package cn.luquba678.entity;

import com.baidu.navisdk.util.common.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import cn.luquba678.utils.DateUtils;

/**
 * Created by zhuchao on 8/10/15.
 */
public class Comment {
    private String content;
    private String comment_time;
    private String nickname;
    private String headpic;
    private String uid;

    public Comment(String content, String comment_time, String nickname, String headpic) {
        this.content = content;
        this.comment_time = comment_time;
        this.nickname = nickname;
        this.headpic = headpic;
    }
    public Comment(){

    }

    public String getComment_time() {
        if (StringUtils.isNotEmpty(comment_time))
            return DateUtils.timeHint(Long.parseLong(comment_time) * 1000, "yyyy年MM月dd日");
        else
            return "";
    }

    public void setComment_time(String comment_time) {
        this.comment_time=comment_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getContent() {
        return content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Comment(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            setComment_time(jsonObject.getString("createtime"));
            setContent(jsonObject.getString("content"));
            setHeadpic(jsonObject.getString("headpic"));
            setNickname(jsonObject.getString("nickname"));
            setUid(jsonObject.getString("uid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
