package com.example.myLibrary.repository;

import com.example.myLibrary.model.UserBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {

    Optional<UserBook> findByUser_IdAndBook_Id(Long userId, Long bookId);

    List<UserBook> findAllByUserIdAndStatusId(Long userId,Long status);

    List<UserBook> findAllByBookId(Long bookId, Pageable pageable);

    int countAllByBookId(Long bookId);

    List<UserBook> findAllByUserIdAndStatusId(Long userId, Long statusId, Pageable pageable);

}
