package com.server.bookstoremanager.user.repository;

import com.server.bookstoremanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
