package com.military.comms.repository;

import com.military.comms.model.MilitaryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MilitaryUserRepository extends JpaRepository<MilitaryUser, Long> {
    Optional<MilitaryUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
