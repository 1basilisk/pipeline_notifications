package com.basilisk.pipeline_notification_service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.basilisk.pipeline_notification_service.entity.Environment;
import com.basilisk.pipeline_notification_service.entity.NotificationRule;
import com.basilisk.pipeline_notification_service.entity.PipelineEvent;
import com.basilisk.pipeline_notification_service.entity.PipelineStatus;
import com.basilisk.pipeline_notification_service.service.NotificationRuleService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class NotificationRuleServiceTest {

    @Autowired
    private NotificationRuleService notificationRuleService;


    @Test
    void matchingRuleShouldReturnTrue() {

        // Arrange
        NotificationRule rule = new NotificationRule(
                "Production failures",
                "sales-etl",
                Environment.PROD,
                PipelineStatus.FAILURE,
                true
        );

        PipelineEvent event = new PipelineEvent(
                "sales-etl",
                "extract-customers",
                Environment.PROD,
                PipelineStatus.FAILURE,
                "Connection timeout",
                LocalDateTime.now()
        );

        // Act
        boolean result = notificationRuleService.matches(rule, event);

        // Assert
        assertTrue(result);
    }


    @Test
    void differentPipelineNameShouldReturnFalse() {

        NotificationRule rule = new NotificationRule(
                "Production failures",
                "sales-etl",
                Environment.PROD,
                PipelineStatus.FAILURE,
                true
        );

        PipelineEvent event = new PipelineEvent(
                "payments-etl",
                "extract-customers",
                Environment.PROD,
                PipelineStatus.FAILURE,
                "Connection timeout",
                LocalDateTime.now()
        );

        boolean result = notificationRuleService.matches(rule, event);

        assertFalse(result);
    }


    @Test
    void differentEnvironmentShouldReturnFalse() {

        NotificationRule rule = new NotificationRule(
                "Production failures",
                "sales-etl",
                Environment.PROD,
                PipelineStatus.FAILURE,
                true
        );

        PipelineEvent event = new PipelineEvent(
                "sales-etl",
                "extract-customers",
                Environment.DEV,
                PipelineStatus.FAILURE,
                "Connection timeout",
                LocalDateTime.now()
        );

        boolean result = notificationRuleService.matches(rule, event);

        assertFalse(result);
    }


    @Test
    void differentStatusShouldReturnFalse() {

        NotificationRule rule = new NotificationRule(
                "Production failures",
                "sales-etl",
                Environment.PROD,
                PipelineStatus.FAILURE,
                true
        );

        PipelineEvent event = new PipelineEvent(
                "sales-etl",
                "extract-customers",
                Environment.PROD,
                PipelineStatus.SUCCESS,
                "No error",
                LocalDateTime.now()
        );

        boolean result = notificationRuleService.matches(rule, event);

        assertFalse(result);
    }
}