package com.crm.repository;

import com.crm.model.Client;
import com.crm.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    List<Comments> findByClient(Client client);
}
