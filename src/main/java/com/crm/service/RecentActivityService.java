package com.crm.service;

import com.crm.model.RecentActivity;
import com.crm.repository.RecentActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecentActivityService {

    private RecentActivityRepository recentActivityRepository;

    @Autowired
    public RecentActivityService(RecentActivityRepository recentActivityRepository) {
        this.recentActivityRepository = recentActivityRepository;
    }

    public List<RecentActivity> findAll() { return recentActivityRepository.findAll(new Sort(Sort.Direction.DESC, "updatedAt")); }
    public RecentActivity findByCommentId(Long commentId) { return recentActivityRepository.findByCommentId(commentId); }
    public RecentActivity save(RecentActivity recentActivity) { return recentActivityRepository.save(recentActivity); }
}
