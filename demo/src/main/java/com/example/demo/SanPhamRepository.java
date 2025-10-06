package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SanPhamRepository {
    private static List<SanPham> dsSanPham = new ArrayList<>();

    static {
        List<NhaSanXuat> nsxList = NhaSanXuatRepository.getAll();

        // Logitech - Sửa đường dẫn ảnh
        dsSanPham.add(new SanPham(1, "Bàn phím Logitech G Pro X", 2590000, 15, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(0)));
        dsSanPham.add(new SanPham(2, "Bàn phím Logitech G915 TKL", 4990000, 8, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(0)));
        dsSanPham.add(new SanPham(3, "Bàn phím Logitech K845", 1590000, 20, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(0)));
        dsSanPham.add(new SanPham(4, "Chuột Logitech G502 Hero", 1890000, 12, "Chuột", "/images/default-mouse.jpg", nsxList.get(0)));
        dsSanPham.add(new SanPham(5, "Chuột Logitech G Pro Wireless", 2990000, 10, "Chuột", "/images/default-mouse.jpg", nsxList.get(0)));
        dsSanPham.add(new SanPham(6, "Chuột Logitech MX Master 3", 3490000, 6, "Chuột", "/images/default-mouse.jpg", nsxList.get(0)));

        // Razer
        dsSanPham.add(new SanPham(7, "Bàn phím Razer BlackWidow V3", 2990000, 10, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(1)));
        dsSanPham.add(new SanPham(8, "Bàn phím Razer Huntsman Elite", 4990000, 7, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(1)));
        dsSanPham.add(new SanPham(9, "Bàn phím Razer Cynosa V2", 1390000, 25, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(1)));
        dsSanPham.add(new SanPham(10, "Chuột Razer DeathAdder V2", 1590000, 20, "Chuột", "/images/default-mouse.jpg", nsxList.get(1)));
        dsSanPham.add(new SanPham(11, "Chuột Razer Viper Ultimate", 3490000, 8, "Chuột", "/images/default-mouse.jpg", nsxList.get(1)));
        dsSanPham.add(new SanPham(12, "Chuột Razer Naga Trinity", 2490000, 9, "Chuột", "/images/default-mouse.jpg", nsxList.get(1)));

        // MCHOSE
        dsSanPham.add(new SanPham(13, "Bàn phím MCHOSE MK1 RGB", 990000, 30, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(2)));
        dsSanPham.add(new SanPham(14, "Bàn phím MCHOSE MK2 Pro", 1590000, 18, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(2)));
        dsSanPham.add(new SanPham(15, "Bàn phím MCHOSE K87", 1290000, 22, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(2)));
        dsSanPham.add(new SanPham(16, "Chuột MCHOSE GM1", 590000, 40, "Chuột", "/images/default-mouse.jpg", nsxList.get(2)));
        dsSanPham.add(new SanPham(17, "Chuột MCHOSE GM2 Ultra", 890000, 15, "Chuột", "/images/default-mouse.jpg", nsxList.get(2)));
        dsSanPham.add(new SanPham(18, "Chuột MCHOSE GM3 Wireless", 1190000, 12, "Chuột", "/images/default-mouse.jpg", nsxList.get(2)));

        // ATK
        dsSanPham.add(new SanPham(19, "Bàn phím ATK RS7", 790000, 25, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(3)));
        dsSanPham.add(new SanPham(20, "Bàn phím ATK RS7 PRO", 1090000, 18, "Bàn phím", "/images/default-keyboard.jpg", nsxList.get(3)));
        dsSanPham.add(new SanPham(21, "Tai nghe ATK Mercury M1", 1290000, 10, "Tai nghe", "/images/default-headphone.jpg", nsxList.get(3)));
        dsSanPham.add(new SanPham(22, "Chuột ATK X1", 490000, 35, "Chuột", "/images/default-mouse.jpg", nsxList.get(3)));
        dsSanPham.add(new SanPham(23, "Chuột ATK A9 Plus", 690000, 28, "Chuột", "/images/default-mouse.jpg", nsxList.get(3)));
        dsSanPham.add(new SanPham(24, "Chuột ATK U2 Plus", 990000, 14, "Chuột", "/images/default-mouse.jpg", nsxList.get(3)));
    }

    public static List<SanPham> getAll() {
        return dsSanPham;
    }

    public static void add(SanPham sp) {
        dsSanPham.add(sp);
    }

    public static SanPham findById(int id) {
        return dsSanPham.stream().filter(sp -> sp.getId() == id).findFirst().orElse(null);
    }

    public static void update(SanPham sp) {
        for (int i = 0; i < dsSanPham.size(); i++) {
            if (dsSanPham.get(i).getId() == sp.getId()) {
                dsSanPham.set(i, sp);
                return;
            }
        }
    }

    public static void delete(int id) {
        dsSanPham.removeIf(sp -> sp.getId() == id);
    }

    // ✅ Thống kê theo loại
    public static long countByLoai(String loai) {
        return dsSanPham.stream().filter(sp -> sp.getLoai().equalsIgnoreCase(loai)).count();
    }

    // ✅ Trả về danh sách sản phẩm theo loại
    public static List<SanPham> getByLoai(String loai) {
        return dsSanPham.stream()
                .filter(sp -> sp.getLoai().equalsIgnoreCase(loai))
                .collect(Collectors.toList());
    }

    // ✅ Tìm kiếm sản phẩm theo tên
    public static List<SanPham> searchByName(String keyword) {
        return dsSanPham.stream()
                .filter(sp -> sp.getTen().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}