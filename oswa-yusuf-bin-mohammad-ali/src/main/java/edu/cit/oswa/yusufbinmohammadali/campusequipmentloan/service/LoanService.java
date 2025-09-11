package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.service;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.*;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final EquipmentRepository equipmentRepository;
    private final StudentRepository studentRepository;
    private final PenaltyStrategy penaltyStrategy;

    public LoanService(LoanRepository loanRepository,
                       EquipmentRepository equipmentRepository,
                       StudentRepository studentRepository) {
        this.loanRepository = loanRepository;
        this.equipmentRepository = equipmentRepository;
        this.studentRepository = studentRepository;
        this.penaltyStrategy = (PenaltyStrategy) new DailyPenaltyStrategy(); // strategy injected here
    }

    @Transactional
    public Loan createLoan(Long studentId, Long equipmentId) throws Throwable {
        Student student = (Student) studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Equipment equipment = (Equipment) equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));

        if (!equipment.getAvailability()) {
            throw new IllegalStateException("Equipment is not available");
        }

        long activeLoansCount = loanRepository.countByStudentAndStatus(student, LoanStatus.ACTIVE);
        if (activeLoansCount >= 2) {
            throw new IllegalStateException("Student already has maximum number of active loans");
        }

        Loan loan = new Loan();
        loan.setStudent(student);
        loan.setEquipment(equipment);
        loan.setStartDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setStatus(LoanStatus.ACTIVE);

        equipment.setAvailability(false);
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    @Transactional
    public long returnLoan(Long loanId) {
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

        // Mark equipment available
        Equipment equipment = loan.getEquipment();
        equipment.setAvailability(true);
        equipmentRepository.save(equipment);

        loanRepository.save(loan);

        // Calculate penalty if any
        return penaltyStrategy.calculatePenalty(daysLate);
    }

    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByAvailabilityTrue();
    }
}
