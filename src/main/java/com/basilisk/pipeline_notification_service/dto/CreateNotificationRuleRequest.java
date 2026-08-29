package com.basilisk.pipeline_notification_service.dto;

import com.basilisk.pipeline_notification_service.entity.Environment;
import com.basilisk.pipeline_notification_service.entity.PipelineStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateNotificationRuleRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String pipelineName;

    @NotNull
    private Environment environment;

    @NotNull
    private PipelineStatus status;

    private boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    

    // getters and setters
}