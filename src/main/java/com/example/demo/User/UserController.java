package com.example.demo.User;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // 이 클래스가 Spring MVC의 컨트롤러임을 나타냄 (뷰 반환)
@RequestMapping("/users") // 모든 메서드의 기본 URL 경로는 "/users"로 설정됨
public class UserController {

    @Autowired // UserRepository를 자동 주입 (사용자 정보 조회/저장용)
    private UserRepository userRepository;

    @Autowired // 비밀번호 암호화를 위한 PasswordEncoder 자동 주입
    private PasswordEncoder passwordEncoder;

    // 생성자를 통한 의존성 주입
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 로그인 폼을 보여주는 GET 요청 처리
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // 회원가입 폼을 보여주는 GET 요청 처리
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup"; // signup.html 반환
    }

    // 회원가입 처리 요청 (POST)
    @PostMapping("/signup")
    public String signup(@RequestParam("name") String name, // 이름 입력값
                         @RequestParam("username") String userId, // 사용자 ID 입력값
                         @RequestParam("password") String password, // 비밀번호 입력값
                         @RequestParam("nickname") String nickname, // 닉네임 입력값
                         Model model) { // 뷰에 전달할 데이터를 담을 모델 객체
        // 이미 존재하는 사용자 ID인지 검사
        if (userRepository.findByUsername(userId).isPresent()) {
            model.addAttribute("error", "이미 사용 중인 아이디입니다."); // 에러 메시지 전달
            return "signup"; // 다시 회원가입 페이지로 이동
        }

        // 이미 존재하는 닉네임인지 검사
        if (userRepository.findByNickname(nickname).isPresent()) {
            model.addAttribute("error", "이미 사용 중인 닉네임입니다."); // 에러 메시지 전달
            return "signup"; // 다시 회원가입 페이지로 이동
        }

        // 새로운 사용자 객체 생성 및 설정
        UserEntity user = new UserEntity();
        user.setName(name); // 이름 설정
        user.setUsername(userId); // 사용자 ID 설정
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화 후 설정
        user.setNickname(nickname); // 닉네임 설정
        user.setRole("USER"); // 기본 권한 설정

        userRepository.save(user); // 사용자 정보 DB에 저장

        return "redirect:/users/signup_complete"; // 회원가입 완료 페이지로 리다이렉트
    }

    // 회원가입 완료 페이지 반환
    @GetMapping("/signup_complete")
    public String signupComplete() {
        return "signup_complete"; // signup_complete.html 반환
    }
}