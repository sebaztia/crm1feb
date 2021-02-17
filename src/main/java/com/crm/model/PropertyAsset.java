package com.crm.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "propery_asset")
public @Data
class PropertyAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int propertyOne;
    private int mortgageOne;

    private int propertyRegisteredOne;
    private int ifJointOne;

    private String lenderDetails;
    private String amountOSOne;
    private String currentValueOne;
    private String propertyHeldOne;
    private String contributeOne;

    private String otherProperty;
    private int mortgageTwo;
    private int propertyRegisteredTwo;
    private int ifJointTwo;
    private String amountOSTwo;
    private String currentValueTwo;
    private String propertyHeldTwo;
    private String contributeTwo;


}
