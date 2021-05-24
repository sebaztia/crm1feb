package com.crm.dto;

import lombok.Data;

public @Data class LetterDto {

    private Long clientI;
    private String deceasedName;
    private String deceasedAddress;
    private String doBirth;
    private String doDeath;
    private String maritalStatus;
    private String doWill;

    private String executorName;
    private String executorAddress;
    private String relationship;
    private String contactNumber;

    private String bankAddress;
    private String ourReference;
    private String yourReference;
    private String estateOf;
    private String accountNumber;
    private String emailTo;


    public LetterDto(Long clientId) {
        this.clientI = clientId;
    }
    public LetterDto() {}
}
