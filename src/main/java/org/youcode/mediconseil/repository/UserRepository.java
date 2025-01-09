package org.youcode.mediconseil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.User;
import java.util.UUID;

public interface UserRepository extends JpaRepository<org.youcode.mediconseil.domain.User, UUID> {




}
