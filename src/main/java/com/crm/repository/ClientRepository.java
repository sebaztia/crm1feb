package com.crm.repository;

import com.crm.model.Client;
import com.crm.model.Company;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByCompany(Company company, Sort sort);
    List<Client> findByCompany(Company company);
    Integer countByClearedTrue();
    Integer countByClearedFalseOrClearedNull();
    Integer countByDeceasedTrue();
}
