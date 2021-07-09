package com.crm.service;

import com.crm.dto.ClientDto;
import com.crm.dto.ElapsedTime;
import com.crm.model.Client;
import com.crm.model.Company;
import com.crm.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client saveClient(Client client) { return clientRepository.save(client); }

    public List<Client> findByCompany(Company company) { return clientRepository.findByCompany(company); }

    public List<Client> findAll() { return clientRepository.findAll(); }

    public Client getClientById(Long id) { return clientRepository.findOne(id); }

    public void deleteCompany(Long id) { clientRepository.delete(id); }

    public Integer countByClearedTrue() { return clientRepository.countByClearedTrue(); }

    public Integer countByClearedFalseOrClearedNull() { return clientRepository.countByClearedFalseOrClearedNull(); }

    public Long countAll() { return  clientRepository.countByIsLeadsNull(); }

    public Integer countByDeceasedTrue() { return clientRepository.countByDeceasedTrue(); }

    public List<ClientDto> findByStatus(String status) {
        return  toDto(clientRepository.findByStatus(status));
    }
    public List<ClientDto> findByStatusAndContactNotIn(String status, String contact) {
        return  toDto(clientRepository.findByStatusAndContactNotIn());
    }

    private ClientDto toDto(Client client) {
        ClientDto dto = new ClientDto();

        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setPriority(client.getPriority());
        dto.setElapsedDays(setElapsed(client.getUpdatedAt()));
        dto.setCompanyName(client.getCompany().getName());
        return dto;
    }

    private /*ElapsedTime*/ Long setElapsed(Date updatedAt) {
        long different = new Date().getTime() - updatedAt.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
/*        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

        return new ElapsedTime(elapsedDays);*/
        return elapsedDays;
    }

    private List<ClientDto> toDto(List< Client > clientList) {
        if (clientList == null) {
            return null;
        }
        return clientList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Client findByCallListId(Integer callListId) {
        return clientRepository.findByCallListId(callListId);
    }

    public List<Client> findClients() { return clientRepository.findByIsLeadsNull(); }

    public List<Client> getTodoList() { return clientRepository.findAllByTodosTrue(); }
}
