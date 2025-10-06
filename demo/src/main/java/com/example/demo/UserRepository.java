package com.example.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    private static List<User> users = new ArrayList<>();
    private static int nextId = 4;

    static {
        users.add(new User(1, "admin", "admin123", "admin@gearstore.com", "ADMIN", "Quản trị viên", "0123456789"));
        users.add(new User(2, "user", "123", "user@gearstore.com", "USER", "Người dùng", "0987654321"));
        users.add(new User(3, "taidao", "123", "john@email.com", "ADMIN", "tai dao", "0912345678"));
        users.add(new User(4, "user2", "123", "user@gearstore.com", "USER", "Người dùng", "0987654321"));
        users.add(new User(5, "user3", "123", "user@gearstore.com", "USER", "Người dùng", "0987654321"));
        users.add(new User(6, "user4", "123", "user@gearstore.com", "USER", "Người dùng", "0987654321"));
    }

    public static List<User> getAll() {
        return users;
    }

    public static List<User> getUsers() {
        return users.stream()
                .filter(user -> "USER".equals(user.getRole()))
                .collect(Collectors.toList());
    }

    public static User findById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static User findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public static User findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public static void add(User user) {
        user.setId(nextId++);
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        users.add(user);
    }

    public static void update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                return;
            }
        }
    }

    public static void delete(int id) {
        users.removeIf(user -> user.getId() == id);
    }

    public static boolean validateUser(String username, String password) {
        User user = findByUsername(username);
        return user != null && user.getPassword().equals(password) && user.isActive();
    }

    public static boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }

    public static boolean emailExists(String email) {
        return findByEmail(email) != null;
    }
}