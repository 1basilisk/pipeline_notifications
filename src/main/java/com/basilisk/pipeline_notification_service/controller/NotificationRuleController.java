package com.basilisk.pipeline_notification_service.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.basilisk.pipeline_notification_service.dto.CreateNotificationRuleRequest;
import com.basilisk.pipeline_notification_service.entity.NotificationRule;
import com.basilisk.pipeline_notification_service.service.NotificationRuleService;
import jakarta.validation.Valid;

@RestController
public class NotificationRuleController {
    private final NotificationRuleService service;

    public NotificationRuleController(NotificationRuleService service) {
        this.service = service;
    }

    @PostMapping("/api/rules")
    public NotificationRule createRule(
            @Valid @RequestBody CreateNotificationRuleRequest request) {

        NotificationRule rule = new NotificationRule(
                request.getPipelineName(),
                request.getName(),
                request.getEnvironment(),
                request.getStatus(),
                request.isEnabled()
        );

        return service.createRule(rule);
    }

    @GetMapping("/api/rules")
    public List<NotificationRule> getAllRules() {
        return service.getAllRules();
    }
    
    @GetMapping("/api/rules/{id}")
    public Optional<NotificationRule> getRuleById(@PathVariable Long id) {
        return service.getRuleById(id);
    }

}
