package com.crm.service;

import com.crm.model.PersonalAsset;
import com.crm.repository.PersonalAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalAssetService {
    private PersonalAssetRepository personalAssetRepository;

    @Autowired
    public PersonalAssetService(PersonalAssetRepository personalAssetRepository) {
        this.personalAssetRepository = personalAssetRepository;
    }

    public PersonalAsset findByClientId(Long clientId) { return personalAssetRepository.findByClientId(clientId); }
    public PersonalAsset savePersonalAsset(PersonalAsset personalAsset) { return  personalAssetRepository.save(personalAsset);}
}
