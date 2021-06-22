package com.crm.repository;

import com.crm.model.EstateProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstatePropertyRepository extends JpaRepository<EstateProperty, Long> {
    EstateProperty findByClientId(Long clientId);
}
