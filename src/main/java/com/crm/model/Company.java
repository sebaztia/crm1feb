package com.crm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "company")
@Getter @Setter
public
class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    private String contact;
    private String address;
   // @NotEmpty
    private String telephone;
  //  @NotEmpty
  //  @Email(message = "Invalid email ID")
    private String email;
    private String website;
    private Boolean inActive;

    @Column(unique = true)
    private String isbn;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Client> clientSet;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Company() {
    }

    public Company(String name, String contact, String address, String telephone, String email, String website, String isbn) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.website = website;
        this.isbn = isbn;
    }

    public Company(Boolean active) {
        this.inActive = active;
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
