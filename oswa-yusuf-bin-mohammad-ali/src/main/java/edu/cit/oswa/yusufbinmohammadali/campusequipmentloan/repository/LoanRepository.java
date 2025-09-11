package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.repository;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Loan;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.LoanStatus;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    long countByStudentAndStatus(Student student, LoanStatus status);

    List<Loan> findByStudentAndStatus(Student student, LoanStatus status);

    List<Loan> findByStatus(LoanStatus status);

}
