package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.controller;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Equipment;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.repository.EquipmentRepository;
import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final LoanService loanService;
    private final EquipmentRepository equipmentRepository;

    public EquipmentController(LoanService loanService, EquipmentRepository equipmentRepository) {
        this.loanService = loanService;
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableEquipment() {
        try {
            List<Equipment> equipmentList = loanService.getAvailableEquipment();
            return ResponseEntity.ok(equipmentList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch available equipment: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createEquipment(@RequestBody Equipment equipment) {
        try {
            Equipment savedEquipment = equipmentRepository.save(equipment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create equipment: " + e.getMessage());
        }
    }
}
