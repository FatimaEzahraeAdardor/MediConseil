package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User User);
    User update(User user);
    Boolean delete(UUID id);
    Optional<User> findByID(UUID id);
    Page<User> getAllUsersPaginated(int page, int size);


}
