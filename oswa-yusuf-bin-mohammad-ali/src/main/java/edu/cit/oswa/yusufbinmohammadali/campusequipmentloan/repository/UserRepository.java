package edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.repository;

import edu.cit.oswa.yusufbinmohammadali.campusequipmentloan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
