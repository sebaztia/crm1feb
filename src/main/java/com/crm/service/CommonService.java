package com.crm.service;

import com.crm.dto.EstateReports;
import com.crm.model.Client;
import com.crm.model.EstateProperty;
import com.crm.model.FamilyContact;
import com.crm.model.KinInfo;
import com.crm.repository.EstatePropertyRepository;
import com.crm.repository.FamilyContactRepository;
import com.crm.repository.KinInfoRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonService {

    private EstatePropertyRepository estatePropertyRepository;
    private KinInfoRepository kinInfoRepository;
    private FamilyContactRepository familyContactRepository;

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(CommonService.class);
    @Autowired
    public CommonService(EstatePropertyRepository estatePropertyRepository, KinInfoRepository kinInfoRepository, FamilyContactRepository familyContactRepository) {
        this.estatePropertyRepository = estatePropertyRepository;
        this.kinInfoRepository = kinInfoRepository;
        this.familyContactRepository = familyContactRepository;
    }

    public EstateProperty getEstateProperty(Long clientId) {
        EstateProperty estateProperty = estatePropertyRepository.findByClientId(clientId);
        if (null == estateProperty) {
            estateProperty = new EstateProperty();
            estateProperty.setClientId(clientId);
        }
        return  estateProperty;
    }

    public KinInfo getKinInfo(Long clientId) {

        KinInfo kinInfo = kinInfoRepository.findByClientId(clientId);
        if (null == kinInfo) {
            kinInfo = new KinInfo();
            kinInfo.setClientId(clientId);
        }
        return kinInfo;
    }

    public FamilyContact getFamilyContacts(Long clientId) {
        FamilyContact familyContact = familyContactRepository.findByClientId(clientId);
        if (null == familyContact) {
            familyContact = new FamilyContact();
            familyContact.setClientId(clientId);
        }
        return familyContact;
    }

    public void saveEstateAsset(EstateProperty estateProperty, KinInfo kinInfo, FamilyContact familyContact) {
        estatePropertyRepository.save(estateProperty);
        kinInfoRepository.save(kinInfo);
        familyContactRepository.save(familyContact);

    }

    @Autowired
    private IEstateDao api;

    public List<EstateReports> findReports() {

       return estatePropertyRepository.findReports().stream().map(x ->
               new EstateReports(Long.valueOf(x[0].toString()), x[1].toString(), Integer.valueOf(x[2].toString()),
                       Integer.valueOf(x[3].toString()), Long.valueOf(x[4].toString()))).collect(Collectors.toList());
    }

    public List<EstateReports> findReports(String eWorth, String textWorth, String eValue, String textValue, String choiceradio, String maritalStatus) {

        return api.searchUser(eWorth, textWorth, eValue, textValue, choiceradio, maritalStatus).stream().map(x ->
                new EstateReports(Long.valueOf(x[0].toString()), x[1].toString(), Integer.valueOf(x[2].toString()),
                        Integer.valueOf(x[3].toString()), Long.valueOf(x[4].toString()))).collect(Collectors.toList());
    }
}
