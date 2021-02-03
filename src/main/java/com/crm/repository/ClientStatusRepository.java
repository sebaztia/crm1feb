package com.crm.repository;

import com.crm.model.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientStatusRepository extends JpaRepository<ClientStatus, Integer> {
}
