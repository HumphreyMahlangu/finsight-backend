package com.finsight.repository;

import com.finsight.model.Conversation;
import com.finsight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserOrderByCreatedAtDesc(User user);
    Optional<Conversation> findByIdAndUser(Long id, User user);
}