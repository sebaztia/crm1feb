package com.crm.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
public @Data class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String author;
    private String username;
    private Long clientId;
    private String clientName;
    private Boolean seen;
    private String mentioned;

    public Notification(String author, String username, Long clientId, String clientName, Boolean seen, String mentioned) {
        this.author = author;
        this.username = username;
        this.clientId = clientId;
        this.clientName = clientName;
        this.seen = seen;
        this.mentioned = mentioned;
    }

    public Notification() {    }
}
