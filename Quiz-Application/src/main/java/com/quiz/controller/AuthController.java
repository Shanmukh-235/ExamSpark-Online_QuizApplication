package com.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quiz.entity.User;
import com.quiz.repo.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Serve login page
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password!");
        }
        return "login"; // login.html
    }

    // Serve registration page
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // register.html
    }

    // Handle login
    @PostMapping("/perform_login")
    public String login(@RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        return userRepository.findByEmail(email)
                .map(user -> {
                    if (user.getPassword().equals(password)) {
                        session.setAttribute("user", user);

                        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                            return "redirect:/admin";
                        } else {
                            return "redirect:/dashboard";
                        }
                    }
                    return "redirect:/login?error=true"; // wrong password
                })
                .orElse("redirect:/login?error=true"); // user not found
    }

    // Handle registration
    @PostMapping("/perform_register")
    public String register(@RequestParam String username,@RequestParam String course,  @RequestParam String email, @RequestParam String password, Model model) {
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setCourse(course); // plain text for now
        user.setRole("USER"); // default role
        userRepository.save(user);

        model.addAttribute("success", "Registration successful! Please login.");
        return "login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login"; // fixed
    }
}
