package com.crm.service;

import com.crm.model.Notification;
import com.crm.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private NotificationRepository repository;

    @Autowired
    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Notification save(Notification notification) {
        return repository.save(notification);
    }

    public List<Notification> getNotificationByMentioned(String mentioned) {
        return repository.findByMentionedAndSeenFalse(mentioned, new Sort(Sort.Direction.DESC, "id"));
    }

    public Notification findOne(long id) { return repository.findOne(id); }
}
