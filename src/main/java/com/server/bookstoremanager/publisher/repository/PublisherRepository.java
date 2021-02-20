package com.server.bookstoremanager.publisher.repository;

import com.server.bookstoremanager.publisher.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
