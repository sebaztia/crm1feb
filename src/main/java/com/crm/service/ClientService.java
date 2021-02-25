package com.crm.service;

import com.crm.model.Client;
import com.crm.model.Company;
import com.crm.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void saveClient(Client client) { clientRepository.save(client); }

    public List<Client> findByCompany(Company company) { return clientRepository.findByCompany(company); }

    public List<Client> findAll() { return clientRepository.findAll(); }

    public Client getClientById(Long id) { return clientRepository.findOne(id); }

    public void deleteCompany(Long id) { clientRepository.delete(id); }

    public Integer countByClearedTrue() { return clientRepository.countByClearedTrue(); }

    public Integer countByClearedFalseOrClearedNull() { return clientRepository.countByClearedFalseOrClearedNull(); }

    public Long countAll() { return  clientRepository.count(); }

    public Integer countByDeceasedTrue() { return clientRepository.countByDeceasedTrue(); }

    public List<Client> findByStatus(String status) { return  clientRepository.findByStatus(status); }
}
