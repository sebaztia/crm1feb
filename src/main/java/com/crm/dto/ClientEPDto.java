package com.crm.dto;

import com.crm.model.EstateProperty;
import com.crm.model.FamilyContact;
import com.crm.model.KinInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientEPDto {

    private Long id;
    private Long clientId;
    List<EstateProperty> estateProperties;
    private KinInfo kinInfo;
    private FamilyContact familyContact;
}
