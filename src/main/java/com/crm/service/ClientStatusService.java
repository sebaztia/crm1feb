package com.crm.service;

import com.crm.model.ClientStatus;
import com.crm.repository.ClientStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientStatusService {

    private ClientStatusRepository repository;

    @Autowired
    public ClientStatusService(ClientStatusRepository repository) {
        this.repository = repository;
    }

    public List<ClientStatus> getAllClientStatus() {
        return repository.findAll();
    }
}
