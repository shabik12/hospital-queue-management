package com.hospital.hospital_queue_management.repository;


import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
    long countByRoleAndEnabled(
            Role role,
            Boolean enabled
    );
}
