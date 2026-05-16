package com.finsight.repository;

import com.finsight.model.Document;
import com.finsight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUserOrderByCreatedAtDesc(User user);
    Optional<Document> findByIdAndUser(Long id, User user);
    List<Document> findByUserAndStatus(User user, Document.Status status);
}