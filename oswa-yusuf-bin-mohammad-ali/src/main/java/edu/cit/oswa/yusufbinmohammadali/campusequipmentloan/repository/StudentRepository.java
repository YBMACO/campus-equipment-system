package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.repository;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentNo(String studentNo);
}
