package com.crm.repository;

import com.crm.dto.EstateReports;
import com.crm.model.EstateProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;


public interface EstatePropertyRepository extends JpaRepository<EstateProperty, Long> {
    EstateProperty findByClientId(Long clientId);
    List<EstateProperty> findAllByClientId(Long clientId);

    @Query(value = "SELECT e.id, c.name, e.worth, e.value, e.client_id FROM estate_property e, client c where c.id = e.client_id;", nativeQuery = true)
    List<Object[]> findReports();

}