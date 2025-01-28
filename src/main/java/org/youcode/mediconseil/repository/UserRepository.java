package org.youcode.mediconseil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);





}
