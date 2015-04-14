package com.prateek.eskores;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by prateek on 16/3/15.
 */
public class ScoreFeed implements Parcelable{

    private int id;
    private String title;
    private String link;
    private String description;
    private String oversPlayed;

    private String bat1;
    private String bat2;

    private String bowl1;

    public String getOversPlayed() {
        return oversPlayed;
    }

    public void setOversPlayed(String oversPlayed) {
        this.oversPlayed = oversPlayed;
    }

    public String getBat1() {
        return bat1;
    }

    public void setBat1(String bat1) {
        this.bat1 = bat1;
    }

    public String getBat2() {
        return bat2;
    }

    public void setBat2(String bat2) {
        this.bat2 = bat2;
    }

    public String getBowl1() {
        return bowl1;
    }

    public void setBowl1(String bowl1) {
        this.bowl1 = bowl1;
    }

    public ScoreFeed(int id, String title, String link, String description) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ScoreFeed{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", oversPlayed='" + oversPlayed + '\'' +
                ", bat1='" + bat1 + '\'' +
                ", bat2='" + bat2 + '\'' +
                ", bowl1='" + bowl1 + '\'' +
                '}';
    }

    public ScoreFeed(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ScoreFeed> CREATOR = new Parcelable.Creator<ScoreFeed>() {
        public ScoreFeed createFromParcel(Parcel in) {
            return new ScoreFeed(in);
        }

        public ScoreFeed[] newArray(int size) {
            return new ScoreFeed[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(oversPlayed);
        dest.writeString(bat1);
        dest.writeString(bat2);
        dest.writeString(bowl1);
    }

    public void readFromParcel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        link = in.readString();
        oversPlayed = in.readString();
        bat1 = in.readString();
        bat2 = in.readString();
        bowl1 = in.readString();
    }
}
