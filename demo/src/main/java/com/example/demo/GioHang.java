package com.example.demo;

public class GioHang {
    private SanPham sanPham;
    private int soLuongMua;
    private double thanhTien;

    public GioHang() {}

    public GioHang(SanPham sanPham, int soLuongMua) {
        this.sanPham = sanPham;
        this.soLuongMua = soLuongMua;
        this.thanhTien = sanPham.getGia() * soLuongMua;
    }

    // Getters and Setters
    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }

    public int getSoLuongMua() { return soLuongMua; }
    public void setSoLuongMua(int soLuongMua) {
        this.soLuongMua = soLuongMua;
        this.thanhTien = this.sanPham.getGia() * soLuongMua;
    }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
}