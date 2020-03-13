package com.example.thweibo;

import android.os.Parcel;
import android.os.Parcelable;

public class MyBlog implements Parcelable {

    private int weiboId;        // 微博的id
    private String nickname;    // 用户昵称
    private String datetime;    // 发微博时间
    private String content;     // 微博内容

    public MyBlog(){}

    public int getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(int weiboId) {
        this.weiboId = weiboId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.weiboId);
        parcel.writeString(this.nickname);
        parcel.writeString(this.datetime);
        parcel.writeString(this.content);
    }

    protected MyBlog(Parcel parcel) {
        this.weiboId = parcel.readInt();
        this.nickname = parcel.readString();
        this.datetime = parcel.readString();
        this.content = parcel.readString();
    }

    public static final Parcelable.Creator<MyBlog> CREATOR = new Parcelable.Creator<MyBlog>() {

        @Override
        public MyBlog createFromParcel(Parcel parcel) {
            return new MyBlog(parcel);
        }

        @Override
        public MyBlog[] newArray(int size) {
            return new MyBlog[size];
        }
    };
}
