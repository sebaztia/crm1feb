package com.crm.repository;

import com.crm.model.FamilyContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyContactRepository extends JpaRepository<FamilyContact, Long> {
    FamilyContact findByClientId(Long clientId);
}
