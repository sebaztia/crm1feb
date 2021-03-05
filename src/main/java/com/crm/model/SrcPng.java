package com.crm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "srcpngimage")
@Getter
@Setter
public class SrcPng {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String author;
    private String fullName;
    private String src;
}
