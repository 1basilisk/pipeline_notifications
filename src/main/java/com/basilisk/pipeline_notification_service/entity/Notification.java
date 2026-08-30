package com.basilisk.pipeline_notification_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PipelineEvent pipelineEvent;

    @ManyToOne
    private NotificationRule notificationRule;

    private String message;

    private LocalDateTime createdAt;

    protected Notification() {
    }

    public Notification(
            PipelineEvent pipelineEvent,
            NotificationRule notificationRule,
            String message) {

        this.pipelineEvent = pipelineEvent;
        this.notificationRule = notificationRule;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
    return id;
}

    public PipelineEvent getPipelineEvent() {
        return pipelineEvent;
    }

    public NotificationRule getNotificationRule() {
        return notificationRule;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
}
}