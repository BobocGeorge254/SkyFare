package org.example.skyfarebackend.repository;

import org.example.skyfarebackend.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAuthorId(Long authorId, Pageable pageable);
    Page<Book> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Book> findByAuthorIdAndCategoryId(Long authorId, Long categoryId, Pageable pageable);
}
