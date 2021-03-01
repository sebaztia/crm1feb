package com.crm.dto;

import lombok.Data;

import java.util.Date;

public @Data
class ClientDto {

    private Long id;
    private String name;
    private String email;
    private int priority;
    private Date updatedAt;
    private ElapsedTime elapsedTime;

}
