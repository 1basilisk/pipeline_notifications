package com.basilisk.pipeline_notification_service.service;

import com.basilisk.pipeline_notification_service.entity.PipelineEvent;
import com.basilisk.pipeline_notification_service.repository.PipelineEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.basilisk.pipeline_notification_service.entity.Notification;
import com.basilisk.pipeline_notification_service.entity.NotificationRule;

@Service
public class PipelineEventService {

    private final PipelineEventRepository repository;
    private final NotificationRuleService notificationRuleService;
    private final NotificationService notificationService;

    public PipelineEventService(
            PipelineEventRepository repository,
            NotificationRuleService notificationRuleService,
            NotificationService notificationService) {

        this.repository = repository;
        this.notificationRuleService = notificationRuleService;
        this.notificationService = notificationService;
    }

    public PipelineEvent createEvent(PipelineEvent event) {

    PipelineEvent savedEvent = repository.save(event);

    List<NotificationRule> enabledRules =
            notificationRuleService.getEnabledRules();

    for (NotificationRule rule : enabledRules) {

        if (notificationRuleService.matches(rule, savedEvent)) {

            String message =
                    "Pipeline " + savedEvent.getPipelineName()
                    + " has status " + savedEvent.getStatus()
                    + " in " + savedEvent.getEnvironment();

            Notification notification =
                    new Notification(
                            savedEvent,
                            rule,
                            message
                    );

            notificationService.createNotification(notification);
        }
    }

    return savedEvent;
}

    public Optional<PipelineEvent> getEventById(Long id) {
        return repository.findById(id);
    }

    public List<PipelineEvent> getAllEvents() {
        return repository.findAll();
    }
}