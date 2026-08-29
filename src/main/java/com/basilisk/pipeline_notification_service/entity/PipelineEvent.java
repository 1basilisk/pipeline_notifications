package com.basilisk.pipeline_notification_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PipelineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pipelineName;

    private String jobName;

    @Enumerated(EnumType.STRING)
    private Environment environment;

    @Enumerated(EnumType.STRING)
    private PipelineStatus status;

    private String errorMessage;

    private LocalDateTime occurredAt;

    private LocalDateTime createdAt;


    protected PipelineEvent() {
        // Required by JPA
    }

    public PipelineEvent(
            String pipelineName,
            String jobName,
            Environment environment,
            PipelineStatus status,
            String errorMessage,
            LocalDateTime occurredAt) {

        this.pipelineName = pipelineName;
        this.jobName = jobName;
        this.environment = environment;
        this.status = status;
        this.errorMessage = errorMessage;
        this.occurredAt = occurredAt;
        this.createdAt = LocalDateTime.now();
    }



    public Long getId() {
        return id;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public PipelineStatus getStatus() {
        return status;
    }

    public void setStatus(PipelineStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}