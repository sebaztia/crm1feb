package com.crm.service;

import com.crm.model.Client;
import com.crm.model.Comments;
import com.crm.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {

    private CommentsRepository commentsRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public List<Comments> findByClient(Client client) {
        return commentsRepository.findByClient(client);
    }

    public void save(Comments comments) {
        commentsRepository.save(comments);
    }
}
