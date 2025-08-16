package com.example.mobileapp;

public class Parking {
    String nazwaParkingu;
    String urlStrony;
    String polozenieNaMapie;

    public Parking(String NazwaParkingu, String UrlStrony, String PolozenieNaMapie) {
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
