package com.crm.service;

import com.crm.model.Contact;
import com.crm.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private ContactRepository repository;

    @Autowired
    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public List<Contact> save(List<Contact> contacts) {
        return repository.save(contacts);
    }

    public List<Contact> findAllByClientId(Long companyId) { return repository.findAllByCompanyId(companyId); }
}
