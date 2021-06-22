package com.crm.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class KinInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String kinname;
    private String kinrelationship;
    private String kinemail;
    private String kinphone;
    private String kinaddress;
    private Long clientId;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

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
}
