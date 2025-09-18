package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.repository;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByAvailabilityTrue();
}
