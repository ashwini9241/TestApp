package com.example.monalisa.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 27/05/16.
 * changed to Parcelable type
 */
public class DummyData implements Parcelable{
    String title;
    String description;
    String imageUrl;

    public DummyData(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    private DummyData(Parcel source) {
        this.title = source.readString();
        this.description = source.readString();
        this.imageUrl = source.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageUrl);

    }

    public static final Parcelable.Creator<DummyData> CREATOR = new Parcelable.Creator<DummyData>(){

        @Override
        public DummyData createFromParcel(Parcel source) {
            return new DummyData(source);
        }

        @Override
        public DummyData[] newArray(int size) {
            return new DummyData[size];
        }
    };
}
