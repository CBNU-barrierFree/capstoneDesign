package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 페이지 GET
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    // 회원가입 처리 POST
    @PostMapping("/signup")
    public String signup(@RequestParam String userId,
                         @RequestParam String password,
                         Model model) {
        if (userRepository.findByUsername(userId).isPresent()) {
            model.addAttribute("error", "이미 사용 중인 아이디입니다.");
            return "signup";
        }

        UserEntity user = new UserEntity();
        user.setUsername(userId);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);

        return "redirect:/users/signup_complete";
    }

    // 회원가입 완료 페이지
    @GetMapping("/signup_complete")
    public String signupComplete() {
        return "signup_complete";
    }

    // 로그인 페이지 GET
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }
}
