package com.example.demo;

public class NhaSanXuat {
    private int id;
    private String ten;
    private String quocGia;
    private String website;
    private String email;
    private String soDienThoai;

    public NhaSanXuat() {}

    public NhaSanXuat(int id, String ten, String quocGia, String website, String email, String soDienThoai) {
        this.id = id;
        this.ten = ten;
        this.quocGia = quocGia;
        this.website = website;
        this.email = email;
        this.soDienThoai = soDienThoai;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public String getQuocGia() { return quocGia; }
    public void setQuocGia(String quocGia) { this.quocGia = quocGia; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
}
