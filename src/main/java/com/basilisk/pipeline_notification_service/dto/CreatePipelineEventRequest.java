package com.basilisk.pipeline_notification_service.dto;

import com.basilisk.pipeline_notification_service.entity.Environment;
import com.basilisk.pipeline_notification_service.entity.PipelineStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreatePipelineEventRequest {
    @NotBlank
    private String pipelineName;
    @NotBlank
    private String jobName;
    @NotNull
    private Environment environment;
    @NotNull
    private PipelineStatus status;
    
    private String errorMessage;
    @NotNull
    private LocalDateTime occurredAt;


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

   
}