package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.controller;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Loan;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // Create a loan for the currently logged-in student
    @PostMapping
    public ResponseEntity<?> createLoan(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam Long equipmentId) {
        try {
            Loan loan = loanService.createLoanForCurrentUser(userDetails, equipmentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(loan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create loan: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnLoan(@PathVariable Long id) {
        try {
            Loan loan = loanService.returnLoanWithPenalty(id);
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to return loan: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllLoans() {
        try {
            List<Loan> loans = loanService.getAllLoans();
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch loans: " + e.getMessage());
        }
    }
}
