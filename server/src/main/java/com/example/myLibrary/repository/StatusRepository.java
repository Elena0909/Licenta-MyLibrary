package com.example.myLibrary.repository;

import com.example.myLibrary.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

    Status findByName(String name);
}
