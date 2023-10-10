package com.example.apozh.users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        try {
            User existingUser = userService.getUserByUsername(username);
            if (existingUser != null) {
                return "Пользователь с таким логином уже существует.";
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userService.registerUser(username, password);

            Cookie cookie = new Cookie("user", URLEncoder.encode(username, "UTF-8"));
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return "Registration successful!";
        } catch (UnsupportedEncodingException e) {
            return "Error encoding username.";
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        User user = userService.getUserByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Cookie cookie;
            try {
                cookie = new Cookie("user", URLEncoder.encode(username, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encoding username.");
            }
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users;
    }
}
