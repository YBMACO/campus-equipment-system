package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.service;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.*;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final EquipmentRepository equipmentRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PenaltyStrategy penaltyStrategy;

    public LoanService(LoanRepository loanRepository,
                       EquipmentRepository equipmentRepository,
                       StudentRepository studentRepository,
                       UserRepository userRepository,
                       PenaltyStrategy penaltyStrategy) {
        this.loanRepository = loanRepository;
        this.equipmentRepository = equipmentRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.penaltyStrategy = penaltyStrategy;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoansByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return loanRepository.findByStudentAndStatus(student, LoanStatus.ACTIVE);
    }

    @Transactional
    public Loan createLoanForCurrentUser(UserDetails userDetails, Long equipmentId) {
        // 1️⃣ Get User entity from UserDetails
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2️⃣ Get associated Student
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // 3️⃣ Get Equipment
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));

        if (!equipment.getAvailability()) {
            throw new IllegalStateException("Equipment is not available");
        }

        // 4️⃣ Check student’s active loans (max 2)
        long activeLoansCount = loanRepository.countByStudentAndStatus(student, LoanStatus.ACTIVE);
        if (activeLoansCount >= 2) {
            throw new IllegalStateException("Student already has maximum number of active loans");
        }

        // 5️⃣ Create Loan
        Loan loan = new Loan();
        loan.setStudent(student);
        loan.setEquipment(equipment);
        loan.setStartDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setStatus(LoanStatus.ACTIVE);

        // 6️⃣ Update equipment availability
        equipment.setAvailability(false);
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnLoanWithPenalty(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new IllegalStateException("Loan is not active");
        }

        LocalDate returnDate = LocalDate.now();
        loan.setReturnDate(returnDate);

        long daysLate = 0;
        if (returnDate.isAfter(loan.getDueDate())) {
            daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
            loan.setStatus(LoanStatus.OVERDUE);
        } else {
            loan.setStatus(LoanStatus.RETURNED);
        }

        long penalty = penaltyStrategy.calculatePenalty(daysLate);
        loan.setPenalty(penalty);

        Equipment equipment = loan.getEquipment();
        equipment.setAvailability(true);
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByAvailabilityTrue();
    }
}
