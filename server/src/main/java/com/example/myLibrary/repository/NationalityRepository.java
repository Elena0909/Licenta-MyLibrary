package com.example.myLibrary.repository;

import com.example.myLibrary.model.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationalityRepository  extends JpaRepository<Nationality, Long> {
}
