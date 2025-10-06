package com.example.demo;

public class SanPham {
    private int id;
    private String ten;
    private double gia;
    private int soLuong;
    private String loai;
    private String hinhAnh;
    private NhaSanXuat nhaSanXuat;

    // Trường tạm để lưu đường dẫn ảnh từ input
    private String tempImagePath;

    public SanPham() {}

    public SanPham(int id, String ten, double gia, int soLuong, String loai, String hinhAnh, NhaSanXuat nhaSanXuat) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
        this.loai = loai;
        this.hinhAnh = hinhAnh;
        this.nhaSanXuat = nhaSanXuat;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public NhaSanXuat getNhaSanXuat() { return nhaSanXuat; }
    public void setNhaSanXuat(NhaSanXuat nhaSanXuat) { this.nhaSanXuat = nhaSanXuat; }

    public String getTempImagePath() { return tempImagePath; }
    public void setTempImagePath(String tempImagePath) { this.tempImagePath = tempImagePath; }
}