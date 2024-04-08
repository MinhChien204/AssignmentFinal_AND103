package com.example.less3.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Sinhvien implements Serializable {

    private String _id;
    private String name;
    private String image;

    private int tuoi;

    private String mssv;


    private boolean daRaTruong;

    public Sinhvien() {
    }

    public Sinhvien(String _id, String name, String image, int tuoi, String mssv, boolean daRaTruong) {
        this._id = _id;
        this.name = name;
        this.image = image;
        this.tuoi = tuoi;
        this.mssv = mssv;
        this.daRaTruong = daRaTruong;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTuoi() {
        return tuoi;
    }

    public void setTuoi(int tuoi) {
        this.tuoi = tuoi;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public boolean isDaRaTruong() {
        return daRaTruong;
    }

    public void setDaRaTruong(boolean daRaTruong) {
        this.daRaTruong = daRaTruong;
    }
}