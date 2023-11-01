package com.example.rollator.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Timestamp;

public class LoopMotivatie implements Parcelable {

    private String Kamer;
    private int Stappen;
    private Timestamp WandelingStart, WandelingStop;

    public LoopMotivatie(String kamer, int stappen, Timestamp wandelingStart, Timestamp wandelingStop) {
        this.Kamer = kamer;
        this.Stappen = stappen;
        this.WandelingStart = wandelingStart;
        this.WandelingStop = wandelingStop;
    }

    protected LoopMotivatie(Parcel in) {
        Kamer = in.readString();
        Stappen = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Kamer);
        dest.writeInt(Stappen);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoopMotivatie> CREATOR = new Creator<LoopMotivatie>() {
        @Override
        public LoopMotivatie createFromParcel(Parcel in) {
            return new LoopMotivatie(in);
        }

        @Override
        public LoopMotivatie[] newArray(int size) {
            return new LoopMotivatie[size];
        }
    };

    public String getKamer() {
        return Kamer;
    }

    public void setKamer(String kamer) {
        Kamer = kamer;
    }

    public int getStappen() {
        return Stappen;
    }

    public void setStappen(int stappen) {
        Stappen = stappen;
    }

    public Timestamp getWandelingStart() {
        return WandelingStart;
    }

    public void setWandelingStart(Timestamp wandelingStart) {
        WandelingStart = wandelingStart;
    }

    public Timestamp getWandelingStop() {
        return WandelingStop;
    }

    public void setWandelingStop(Timestamp wandelingStop) {
        WandelingStop = wandelingStop;
    }
}
