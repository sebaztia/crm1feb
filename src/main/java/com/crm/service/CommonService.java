package com.crm.service;

import com.crm.dto.ClientEPDto;
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

import java.util.ArrayList;
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

    private List<EstateProperty> getEstateProperties(long clientId) {
        List<EstateProperty> estateProperties = estatePropertyRepository.findAllByClientId(clientId);
        if (null == estateProperties || estateProperties.size() == 0) {
            estateProperties = new ArrayList<>();
            estateProperties.add(new EstateProperty(clientId));
        }
        return estateProperties;
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

    public void saveEstateAsset(ClientEPDto clientEPDto) {

        estatePropertyRepository.save(clientEPDto.getEstateProperties());
        kinInfoRepository.save(clientEPDto.getKinInfo());
        familyContactRepository.save(clientEPDto.getFamilyContact());

    }

    @Autowired
    private IEstateDao api;

    public List<EstateReports> findReports() {

       return estatePropertyRepository.findReports().stream().map(x ->
               new EstateReports(Long.valueOf(x[0].toString()), x[1].toString(), Double.valueOf(x[2].toString()),
                       Double.valueOf(x[3].toString()), Long.valueOf(x[4].toString()))).collect(Collectors.toList());
    }

    public List<EstateReports> findReports(String eWorth, String textWorth, String eValue, String textValue, String choiceradio, String maritalStatus) {

        return api.searchUser(eWorth, textWorth, eValue, textValue, choiceradio, maritalStatus).stream().map(x ->
                new EstateReports(Long.valueOf(x[0].toString()), x[1].toString(), Double.valueOf(x[2].toString()),
                        Double.valueOf(x[3].toString()), Long.valueOf(x[4].toString()))).collect(Collectors.toList());
    }

    public ClientEPDto getClientEP(long clientId) {
        ClientEPDto clientEPDto = new ClientEPDto();
        clientEPDto.setClientId(clientId);
        clientEPDto.setFamilyContact(getFamilyContacts(clientId));
        clientEPDto.setKinInfo(getKinInfo(clientId));
        clientEPDto.setEstateProperties(getEstateProperties(clientId));

        return clientEPDto;
    }

    public void deleteComment(Long deleteEPId) {
        estatePropertyRepository.delete(deleteEPId);
    }
}
