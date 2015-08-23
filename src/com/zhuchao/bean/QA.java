package com.zhuchao.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhuchao on 8/21/15.
 */
public class QA implements Parcelable{
    private String question;
    private String answer;

    public QA(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {

        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String anwser) {
        this.answer = anwser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
    }
    public static final Creator<QA>CREATOR=new Creator<QA>() {
        @Override
        public QA createFromParcel(Parcel source) {
            return new QA(source);
        }

        @Override
        public QA[] newArray(int size) {
            return new QA[size];
        }
    };
    private QA(Parcel parcel){
        question=parcel.readString();
        answer=parcel.readString();
    }
    public QA(String content){
        try {
            JSONObject object=new JSONObject(content);
            setQuestion(object.getString("question"));
            setAnswer(object.getString("answer"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
