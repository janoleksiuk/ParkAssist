package com.example.mobileapp;

public class ParkingP_R {
    String nazwaParkingu;
    String urlStrony;
    String polozenieNaMapie;

    public ParkingP_R(String NazwaParkingu,String UrlStrony, String PolozenieNaMapie) {
        this.nazwaParkingu = NazwaParkingu;
        this.urlStrony = UrlStrony;
        this.polozenieNaMapie = PolozenieNaMapie;
    }

    public String getNazwaParkingu() {
        return nazwaParkingu;
    }

    public String getUrlStrony() {
        return urlStrony;
    }

    public String getPolozenieNaMapie() {
        return polozenieNaMapie;
    }

}
