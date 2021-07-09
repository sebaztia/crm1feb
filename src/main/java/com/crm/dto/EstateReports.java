package com.crm.dto;

import lombok.Data;

import java.util.Date;

public @Data class EstateReports {

    private Long id;
    private String clientName;
    private Double estateWorth;
    private Double estateValue;
    private Long clientId;

    public EstateReports(Long id, String clientName, Double estateWorth, Double estateValue, Long clientId) {
        this.id = id;
        this.clientName = clientName;
        this.estateWorth = estateWorth;
        this.estateValue = estateValue;
        this.clientId = clientId;
    }

}
