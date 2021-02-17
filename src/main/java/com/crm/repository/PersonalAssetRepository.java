package com.crm.repository;

import com.crm.model.PersonalAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalAssetRepository extends JpaRepository<PersonalAsset, Integer> { PersonalAsset findByClientId(Long clientId);}
