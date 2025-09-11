package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.controller;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Loan;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
     private final LoanService loanService;

     public LoanController(LoanService loanService) {
         this.loanService = loanService;

     }

     @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Loan createLoan(@RequestParam Long StudentId, @RequestParam Long equipmentId) throws Throwable {
         return loanService.createLoan(StudentId, equipmentId);
     }

     @PostMapping("/{id}/return")
    public long returnLoan(@PathVariable Long id) {
         return loanService.returnLoan(id);
     }
}
