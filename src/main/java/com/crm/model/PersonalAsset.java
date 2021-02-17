package com.crm.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "personal_asset")
public @Data
class PersonalAsset {

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

    private int referralYes;
    @Column(length = 2000)
    private String financialDetails;
    private String estateValue;
    private String taxToPay;

    private Long clientId;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @PrePersist
    public void onPrePersist() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = new Date();
    }

    private String baDetails;
    private String baClient1;
    private String baClient2;
    private String baJoint;

    private String ssDetails;
    private String ssClient1;
    private String ssClient2;
    private String ssJoint;

    private String itDetails;
    private String itClient1;
    private String itClient2;
    private String itJoint;

    private String bsaDetails;
    private String bsaClient1;
    private String bsaClient2;
    private String bsaJoint;

    private String nspbDetails;
    private String nspbClient1;
    private String nspbClient2;
    private String nspbJoint;

    private String cashDetails;
    private String cashClient1;
    private String cashClient2;
    private String cashJoint;

    private String cvDetails;
    private String cvClient1;
    private String cvClient2;
    private String cvJoint;

    private String fpDetails;
    private String fpClient1;
    private String fpClient2;
    private String fpJoint;

    private String otherDetails;
    private String otherClient1;
    private String otherClient2;
    private String otherJoint;

    private String totalDetails;
    private String totalClient1;
    private String totalClient2;
    private String totalJoint;

    private String lifeAssClient1;
    private String lifeAssClient2;
    private String lifeAssJoint;
}
