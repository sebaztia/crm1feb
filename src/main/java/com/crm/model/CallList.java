package com.crm.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "call_list")
public @Data
class CallList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   /* @Column(name = "record_date")
   // @DateTimeFormat(pattern = "yyyy-MM-dd")
  //  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date recordDate;*/

    @NotEmpty
    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "ref_number")
    private String refNumber;

    @Column(name = "client_id")
    private Long clientId;

    @NotEmpty
    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "staff_name")
    private String staffName;
    @NotEmpty
    @Column(name = "query")
    private String query;

    @Column(name = "email_check")
    private Boolean emailCheck;
    @Column(name = "call_check")
    private Boolean callCheck;
    @Column(name = "email_done")
    private Boolean emailDone;
    @Column(name = "call_done")
    private Boolean callDone;
    private Boolean isLeads;

    @Column(name = "archive")
    private Boolean archive;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

   /* @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;*/

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
