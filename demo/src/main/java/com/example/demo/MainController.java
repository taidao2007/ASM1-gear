package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("gioHang")
public class MainController {

    @ModelAttribute("gioHang")
    public List<GioHang> gioHang() {
        return new ArrayList<>();
    }

    // Login page
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        if (UserRepository.validateUser(username, password)) {
            return "redirect:/san-pham";
        } else {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            model.addAttribute("user", new User());
            return "login";
        }
    }

    // Register page
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user,
                                  @RequestParam String confirmPassword,
                                  Model model) {

        try {
            // Validation
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                model.addAttribute("error", "Tên đăng nhập không được để trống!");
                return "register";
            }

            if (UserRepository.usernameExists(user.getUsername())) {
                model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
                return "register";
            }

            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                model.addAttribute("error", "Email không được để trống!");
                return "register";
            }

            if (UserRepository.emailExists(user.getEmail())) {
                model.addAttribute("error", "Email đã được sử dụng!");
                return "register";
            }

            if (user.getPassword() == null || user.getPassword().length() < 6) {
                model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
                return "register";
            }

            if (!user.getPassword().equals(confirmPassword)) {
                model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
                return "register";
            }

            // Set default values for optional fields
            if (user.getFullName() == null) {
                user.setFullName("");
            }
            if (user.getPhone() == null) {
                user.setPhone("");
            }

            // Set default role and add user
            user.setRole("USER");
            UserRepository.add(user);

            model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "login";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi đăng ký: " + e.getMessage());
            return "register";
        }
    }

    // User management (Admin only)
// User management (Admin only)
    @GetMapping("/quan-ly-tai-khoan")
    public String quanLyTaiKhoan(Model model) {
        List<User> users = UserRepository.getUsers();

        // Tính toán thống kê trong controller
        long totalUsers = users.size();
        long activeUsers = users.stream().filter(User::isActive).count();
        long inactiveUsers = totalUsers - activeUsers;

        model.addAttribute("users", users);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("inactiveUsers", inactiveUsers);

        return "quan-ly-tai-khoan";
    }

    @GetMapping("/quan-ly-tai-khoan/edit/{id}")
    public String editTaiKhoan(@PathVariable int id, Model model) {
        User user = UserRepository.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("users", UserRepository.getUsers());
        return "quan-ly-tai-khoan";
    }

    @PostMapping("/quan-ly-tai-khoan/save")
    public String saveTaiKhoan(@ModelAttribute User user) {
        User existingUser = UserRepository.findById(user.getId());
        if (existingUser != null) {
            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setActive(user.isActive());
            UserRepository.update(existingUser);
        }
        return "redirect:/quan-ly-tai-khoan";
    }

    @GetMapping("/quan-ly-tai-khoan/delete/{id}")
    public String deleteTaiKhoan(@PathVariable int id) {
        UserRepository.delete(id);
        return "redirect:/quan-ly-tai-khoan";
    }

    @GetMapping("/quan-ly-tai-khoan/toggle/{id}")
    public String toggleTaiKhoan(@PathVariable int id) {
        User user = UserRepository.findById(id);
        if (user != null) {
            user.setActive(!user.isActive());
            UserRepository.update(user);
        }
        return "redirect:/quan-ly-tai-khoan";
    }

    // Product management
    @GetMapping("/san-pham")
    public String sanPham(Model model, @RequestParam(required = false) String search) {
        List<SanPham> dsSanPham;
        if (search != null && !search.trim().isEmpty()) {
            dsSanPham = SanPhamRepository.searchByName(search);
            model.addAttribute("searchKeyword", search);
        } else {
            dsSanPham = SanPhamRepository.getAll();
        }
        model.addAttribute("dsSanPham", dsSanPham);
        model.addAttribute("dsNSX", NhaSanXuatRepository.getAll());
        model.addAttribute("sp", new SanPham());
        return "san-pham";
    }

    @GetMapping("/san-pham/edit/{id}")
    public String editSanPham(@PathVariable int id, Model model) {
        SanPham sp = SanPhamRepository.findById(id);
        model.addAttribute("sp", sp);
        model.addAttribute("dsNSX", NhaSanXuatRepository.getAll());
        model.addAttribute("dsSanPham", SanPhamRepository.getAll());
        return "san-pham";
    }

    @PostMapping("/san-pham/save")
    public String saveSanPham(@ModelAttribute SanPham sp,
                              @RequestParam("nsxId") int nsxId) {

        // Tự động chọn ảnh mặc định dựa theo loại sản phẩm
        String defaultImage = getDefaultImageByType(sp.getLoai());
        sp.setHinhAnh(defaultImage);

        sp.setNhaSanXuat(NhaSanXuatRepository.findById(nsxId));

        if (sp.getId() == 0) {
            // Generate new ID
            int newId = SanPhamRepository.getAll().stream()
                    .mapToInt(SanPham::getId)
                    .max()
                    .orElse(0) + 1;
            sp.setId(newId);
            SanPhamRepository.add(sp);
        } else {
            SanPhamRepository.update(sp);
        }
        return "redirect:/san-pham";
    }

    // Hàm lấy ảnh mặc định theo loại sản phẩm
    private String getDefaultImageByType(String loai) {
        if (loai == null) return "/images/default-product.jpg";

        switch (loai) {
            case "Bàn phím":
                return "/images/default-keyboard.jpg";
            case "Chuột":
                return "/images/default-mouse.jpg";
            case "Tai nghe":
                return "/images/default-headphone.jpg";
            default:
                return "/images/default-product.jpg";
        }
    }

    @GetMapping("/san-pham/delete/{id}")
    public String deleteSanPham(@PathVariable int id) {
        SanPhamRepository.delete(id);
        return "redirect:/san-pham";
    }

    // Manufacturer management
    @GetMapping("/nha-san-xuat")
    public String nhaSanXuat(Model model) {
        model.addAttribute("dsNSX", NhaSanXuatRepository.getAll());
        model.addAttribute("nsx", new NhaSanXuat());
        return "nha-san-xuat";
    }

    @GetMapping("/nha-san-xuat/edit/{id}")
    public String editNhaSanXuat(@PathVariable int id, Model model) {
        NhaSanXuat nsx = NhaSanXuatRepository.findById(id);
        model.addAttribute("nsx", nsx);
        model.addAttribute("dsNSX", NhaSanXuatRepository.getAll());
        return "nha-san-xuat";
    }

    @PostMapping("/nha-san-xuat/save")
    public String saveNhaSanXuat(@ModelAttribute NhaSanXuat nsx) {
        if (nsx.getId() == 0) {
            int newId = NhaSanXuatRepository.getAll().stream()
                    .mapToInt(NhaSanXuat::getId)
                    .max()
                    .orElse(0) + 1;
            nsx.setId(newId);
            NhaSanXuatRepository.add(nsx);
        } else {
            NhaSanXuatRepository.update(nsx);
        }
        return "redirect:/nha-san-xuat";
    }

    @GetMapping("/nha-san-xuat/delete/{id}")
    public String deleteNhaSanXuat(@PathVariable int id) {
        NhaSanXuatRepository.delete(id);
        return "redirect:/nha-san-xuat";
    }

    // Statistics
    @GetMapping("/thong-ke")
    public String thongKe(Model model) {
        List<ThongKe> thongKeList = NhaSanXuatRepository.getAll().stream()
                .map(nsx -> {
                    long chuot = SanPhamRepository.getAll().stream()
                            .filter(sp -> sp.getNhaSanXuat().getId() == nsx.getId() && "Chuột".equals(sp.getLoai()))
                            .count();
                    long phim = SanPhamRepository.getAll().stream()
                            .filter(sp -> sp.getNhaSanXuat().getId() == nsx.getId() && "Bàn phím".equals(sp.getLoai()))
                            .count();
                    long tainghe = SanPhamRepository.getAll().stream()
                            .filter(sp -> sp.getNhaSanXuat().getId() == nsx.getId() && "Tai nghe".equals(sp.getLoai()))
                            .count();
                    long tong = chuot + phim + tainghe;
                    return new ThongKe(nsx.getTen(), chuot, phim, tainghe, tong);
                })
                .collect(Collectors.toList());
        model.addAttribute("thongKeList", thongKeList);
        return "thong-ke";
    }

    // Shopping page
    @GetMapping("/mua-hang")
    public String muaHang(Model model,
                          @RequestParam(required = false) String search,
                          @ModelAttribute("gioHang") List<GioHang> gioHang) {
        List<SanPham> dsSanPham;
        if (search != null && !search.trim().isEmpty()) {
            dsSanPham = SanPhamRepository.searchByName(search);
            model.addAttribute("searchKeyword", search);
        } else {
            dsSanPham = SanPhamRepository.getAll();
        }

        // Tính tổng tiền giỏ hàng
        double tongTien = gioHang.stream().mapToDouble(GioHang::getThanhTien).sum();

        model.addAttribute("dsSanPham", dsSanPham);
        model.addAttribute("gioHang", gioHang);
        model.addAttribute("tongTien", tongTien);
        return "mua-hang";
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/mua-hang/them")
    public String themVaoGioHang(@RequestParam int productId,
                                 @RequestParam int soLuong,
                                 @ModelAttribute("gioHang") List<GioHang> gioHang,
                                 Model model) {

        SanPham sanPham = SanPhamRepository.findById(productId);

        if (sanPham != null && soLuong > 0 && soLuong <= sanPham.getSoLuong()) {
            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            GioHang existingItem = gioHang.stream()
                    .filter(item -> item.getSanPham().getId() == productId)
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                // Cập nhật số lượng nếu đã có
                int newQuantity = existingItem.getSoLuongMua() + soLuong;
                if (newQuantity <= sanPham.getSoLuong()) {
                    existingItem.setSoLuongMua(newQuantity);
                }
            } else {
                // Thêm mới vào giỏ hàng
                gioHang.add(new GioHang(sanPham, soLuong));
            }
        }

        return "redirect:/mua-hang";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @GetMapping("/mua-hang/xoa/{index}")
    public String xoaKhoiGioHang(@PathVariable int index,
                                 @ModelAttribute("gioHang") List<GioHang> gioHang) {
        if (index >= 0 && index < gioHang.size()) {
            gioHang.remove(index);
        }
        return "redirect:/mua-hang";
    }

    // Thanh toán
    @PostMapping("/mua-hang/thanh-toan")
    public String thanhToan(@ModelAttribute("gioHang") List<GioHang> gioHang) {
        // Cập nhật số lượng tồn kho
        for (GioHang item : gioHang) {
            SanPham sanPham = item.getSanPham();
            int soLuongMua = item.getSoLuongMua();

            if (sanPham.getSoLuong() >= soLuongMua) {
                // Trừ số lượng tồn kho
                sanPham.setSoLuong(sanPham.getSoLuong() - soLuongMua);
                SanPhamRepository.update(sanPham);
            }
        }

        // Xóa giỏ hàng sau khi thanh toán
        gioHang.clear();

        return "redirect:/mua-hang?success=true";
    }
}

class ThongKe {
    private String tenNSX;
    private long chuot;
    private long phim;
    private long tainghe;
    private long tong;

    public ThongKe(String tenNSX, long chuot, long phim, long tainghe, long tong) {
        this.tenNSX = tenNSX;
        this.chuot = chuot;
        this.phim = phim;
        this.tainghe = tainghe;
        this.tong = tong;
    }

    // Getters and Setters
    public String getTenNSX() { return tenNSX; }
    public void setTenNSX(String tenNSX) { this.tenNSX = tenNSX; }
    public long getChuot() { return chuot; }
    public void setChuot(long chuot) { this.chuot = chuot; }
    public long getPhim() { return phim; }
    public void setPhim(long phim) { this.phim = phim; }
    public long getTainghe() { return tainghe; }
    public void setTainghe(long tainghe) { this.tainghe = tainghe; }
    public long getTong() { return tong; }
    public void setTong(long tong) { this.tong = tong; }
}