package com.basilisk.pipeline_notification_service.service;

import com.basilisk.pipeline_notification_service.entity.NotificationRule;
import com.basilisk.pipeline_notification_service.repository.NotificationRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationRuleService {

    private final NotificationRuleRepository repository;

    public NotificationRuleService(NotificationRuleRepository repository) {
        this.repository = repository;
    }

    public Optional<NotificationRule> getRuleById(Long id) {
        return repository.findById(id);
    }

    public List<NotificationRule> getAllRules() {
        return repository.findAll();
    }

    public NotificationRule createRule(NotificationRule rule) {
        return repository.save(rule);
    }
}