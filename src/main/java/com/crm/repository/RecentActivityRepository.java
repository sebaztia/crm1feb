package com.crm.repository;

import com.crm.model.RecentActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentActivityRepository extends JpaRepository<RecentActivity, Long> {
    public RecentActivity findByCommentId(Long commentId);
}
