package com.crm.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recent_activity")
@Getter
@Setter
public class RecentActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String status;
    @Column(length = 2000)
    private String onMessage;
    private String type;
    private String author;
    private String src;
    private Long commentId;
    private Long clientId;

    @Column(length = 25)
    private String moveFrom;
    @Column(length = 25)
    private String moveTo;

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

    public RecentActivity() { }

    public RecentActivity(String status, String onMessage, long commentId, String type, String author, String src) {
        this.status = status;
        this.onMessage = onMessage;
        this.commentId = commentId;
        this.type = type;
        this.author = author;
        this.src = src;
    }

    public RecentActivity(String status, String type, String author, String src, String moveFrom, String moveTo) {
        this.status = status;
        this.type = type;
        this.author = author;
        this.src = src;
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }

    public RecentActivity(String status, String type, String author, String src, Long clientId) {
        this.status = status;
        this.type = type;
        this.author = author;
        this.src = src;
        this.clientId = clientId;
    }
}
