package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class NhaSanXuatRepository {
    private static List<NhaSanXuat> dsNSX = new ArrayList<>();

    static {
        dsNSX.add(new NhaSanXuat(1, "Logitech", "Thụy Sĩ", "https://www.logitech.com", "support@logitech.com", "+41-44-123456"));
        dsNSX.add(new NhaSanXuat(2, "Razer", "Mỹ", "https://www.razer.com", "support@razer.com", "+1-800-765432"));
        dsNSX.add(new NhaSanXuat(3, "MCHOSE", "Trung Quốc", "https://www.mchose.cn", "info@mchose.cn", "+86-10-987654"));
        dsNSX.add(new NhaSanXuat(4, "ATK", "Trung Quốc", "https://www.akt.vn", "contact@akt.vn", "+84-28-111222"));
    }

    public static List<NhaSanXuat> getAll() {
        return dsNSX;
    }

    public static void add(NhaSanXuat nsx) {
        dsNSX.add(nsx);
    }

    public static NhaSanXuat findById(int id) {
        return dsNSX.stream().filter(nsx -> nsx.getId() == id).findFirst().orElse(null);
    }

    public static void update(NhaSanXuat nsx) {
        for (int i = 0; i < dsNSX.size(); i++) {
            if (dsNSX.get(i).getId() == nsx.getId()) {
                dsNSX.set(i, nsx);
                return;
            }
        }
    }

    public static void delete(int id) {
        dsNSX.removeIf(nsx -> nsx.getId() == id);
    }
}
