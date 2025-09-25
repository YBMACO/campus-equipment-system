package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.controller;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Role;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Student;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.User;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.repository.StudentRepository;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.repository.UserRepository;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.service.CustomUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final StudentRepository studentRepository;

    public AuthController(UserRepository userRepository,
                          CustomUserDetailsService customUserDetailsService,
                          StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup-submit")
    public String signupSubmit(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String studentNo,
                               @RequestParam String name,
                               @RequestParam String email) {

        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/auth/signup?error";
        }

        // 1. Register User
        User user = customUserDetailsService.registerUser(username, password, Role.STUDENT);

        // 2. Create Student linked to User
        Student student = new Student(studentNo, name, email, user);
        studentRepository.save(student);

        return "redirect:/auth/login?signupSuccess";
    }

    // ✅ Custom login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}

