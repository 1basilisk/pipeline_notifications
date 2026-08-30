package com.basilisk.pipeline_notification_service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.basilisk.pipeline_notification_service.entity.Environment;
import com.basilisk.pipeline_notification_service.entity.NotificationRule;
import com.basilisk.pipeline_notification_service.entity.PipelineEvent;
import com.basilisk.pipeline_notification_service.entity.PipelineStatus;
import com.basilisk.pipeline_notification_service.repository.NotificationRepository;
import com.basilisk.pipeline_notification_service.repository.NotificationRuleRepository;
import com.basilisk.pipeline_notification_service.repository.PipelineEventRepository;
import com.basilisk.pipeline_notification_service.service.PipelineEventService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PipelineEventServiceTest {

    @Autowired
    private PipelineEventService pipelineEventService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationRuleRepository notificationRuleRepository;

    @Autowired
    private PipelineEventRepository pipelineEventRepository;


    @BeforeEach
    void cleanDatabase() {

        // Notifications reference events and rules, so delete them first
        notificationRepository.deleteAll();

        pipelineEventRepository.deleteAll();
        notificationRuleRepository.deleteAll();
    }


    @Test
    void matchingRuleShouldCreateNotification() {

        // Arrange
        NotificationRule rule = new NotificationRule(
                "Production failures",
                "sales-etl",
                Environment.PROD,
                PipelineStatus.FAILURE,
                true
        );

        notificationRuleRepository.save(rule);

        PipelineEvent event = new PipelineEvent(
                "sales-etl",
                "extract-customers",
                Environment.PROD,
                PipelineStatus.FAILURE,
                "Connection timeout",
                LocalDateTime.now()
        );

        // Act
        pipelineEventService.createEvent(event);

        // Assert
        assertEquals(1, notificationRepository.count());
    }


    @Test
    void nonMatchingRuleShouldNotCreateNotification() {

        // Arrange
        NotificationRule rule = new NotificationRule(
                "Production failures",
                "sales-etl",
                Environment.PROD,
                PipelineStatus.FAILURE,
                true
        );

        notificationRuleRepository.save(rule);

        // Different environment: DEV instead of PROD
        PipelineEvent event = new PipelineEvent(
                "sales-etl",
                "extract-customers",
                Environment.DEV,
                PipelineStatus.FAILURE,
                "Connection timeout",
                LocalDateTime.now()
        );

        // Act
        pipelineEventService.createEvent(event);

        // Assert
        assertEquals(0, notificationRepository.count());
    }


    @Test
    void disabledMatchingRuleShouldNotCreateNotification() {

        // Arrange
        NotificationRule rule = new NotificationRule(
                "Disabled production failures",
                "sales-etl",
                Environment.PROD,
                PipelineStatus.FAILURE,
                false
        );

        notificationRuleRepository.save(rule);

        // This event matches the rule,
        // but the rule is disabled
        PipelineEvent event = new PipelineEvent(
                "sales-etl",
                "extract-customers",
                Environment.PROD,
                PipelineStatus.FAILURE,
                "Connection timeout",
                LocalDateTime.now()
        );

        // Act
        pipelineEventService.createEvent(event);

        // Assert
        assertEquals(0, notificationRepository.count());
    }
}