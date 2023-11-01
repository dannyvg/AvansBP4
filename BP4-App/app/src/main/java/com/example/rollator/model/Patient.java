package com.example.rollator.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class Patient implements Parcelable {
    private String Bejaardentehuis, Naam, Telefoon, Adres, Postcode, Kamer;
    private Date Geboortedatum, VerblijftSinds;
//    private int Kamer;

    protected Patient(Parcel in) {
        Bejaardentehuis = in.readString();
        Naam = in.readString();
        Telefoon = in.readString();
        Adres = in.readString();
        Postcode = in.readString();
        Kamer = in.readString();
//        Kamer = in.readInt();
    }

    public Patient(String bejaardentehuis, String naam, String telefoon, String adres, String postcode, Date geboortedatum, Date verblijftSinds, String kamer) {
        this.Bejaardentehuis = bejaardentehuis;
        this.Naam = naam;
        this.Telefoon = telefoon;
        this.Adres = adres;
        this.Postcode = postcode;
        this.Geboortedatum = geboortedatum;
        this.VerblijftSinds = verblijftSinds;
        this.Kamer = kamer;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Bejaardentehuis);
        dest.writeString(Naam);
        dest.writeString(Telefoon);
        dest.writeString(Adres);
        dest.writeString(Postcode);
        dest.writeString(Kamer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public String getBejaardentehuis() {
        return Bejaardentehuis;
    }

    public void setBejaardentehuis(String bejaardentehuis) {
        Bejaardentehuis = bejaardentehuis;
    }

    public String getNaam() {
        return Naam;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }

    public String getTelefoon() {
        return Telefoon;
    }

    public void setTelefoon(String telefoon) {
        Telefoon = telefoon;
    }

    public String getAdres() {
        return Adres;
    }

    public void setAdres(String adres) {
        Adres = adres;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public Date getGeboortedatum() {
        return Geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        Geboortedatum = geboortedatum;
    }

    public Date getVerblijftSinds() {
        return VerblijftSinds;
    }

    public void setVerblijftSinds(Date verblijftSinds) {
        VerblijftSinds = verblijftSinds;
    }

    public String getKamer() {
        return Kamer;
    }

    public void setKamer(String kamer) {
        Kamer = kamer;
    }
}
