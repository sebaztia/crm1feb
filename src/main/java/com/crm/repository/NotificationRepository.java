package com.crm.repository;

import com.crm.model.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMentionedAndSeenFalse(String mentioned, Sort sort);
}
