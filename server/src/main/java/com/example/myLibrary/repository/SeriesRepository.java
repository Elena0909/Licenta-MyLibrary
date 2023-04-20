package com.example.myLibrary.repository;

import com.example.myLibrary.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {
}
