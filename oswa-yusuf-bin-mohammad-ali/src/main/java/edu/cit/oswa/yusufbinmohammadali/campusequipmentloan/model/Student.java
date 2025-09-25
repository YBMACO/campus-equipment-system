package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String studentNo;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Student() {}

    public Student(String studentNo, String name, String email, User user) {
        this.studentNo = studentNo;
        this.name = name;
        this.email = email;
        this.user = user;
    }

    // Getters and setters...
}


