package com.crm.service;

import com.crm.model.Client;
import com.crm.model.Comments;
import com.crm.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        return commentsRepository.findByClient(client, new Sort(Sort.Direction.DESC, "id"));
    }

    public Comments save(Comments comments) {
        return commentsRepository.save(comments);
    }

    public Comments findOne(Long id) { return commentsRepository.findOne(id); }
    public void deleteComment(Long id) { commentsRepository.delete(id);}
}
