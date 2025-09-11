package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.controller;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Equipment;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.service.LoanService;
import jakarta.persistence.GeneratedValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Equipment")
public class EquipmentController {
    private final LoanService loanService;

    public EquipmentController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/available")
    public List<Equipment> getAvailableEquipment() {
        return loanService.getAvailableEquipment();
    }
}
