package com.crm.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "client")
@Getter @Setter
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String contact;
    @Email
    private String email;
    private String address;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Comments> commentsSet;

    public Client() {
    }

    public Client(String name, String phone, String contact, String address, Company company) {
        this.name = name;
        this.phone = phone;
        this.contact = contact;
        this.address = address;
        this.company = company;
    }

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
