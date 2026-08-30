package com.basilisk.pipeline_notification_service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import com.basilisk.pipeline_notification_service.entity.Environment;
import com.basilisk.pipeline_notification_service.entity.Notification;
import com.basilisk.pipeline_notification_service.entity.NotificationRule;
import com.basilisk.pipeline_notification_service.entity.PipelineEvent;
import com.basilisk.pipeline_notification_service.entity.PipelineStatus;
import com.basilisk.pipeline_notification_service.repository.NotificationRepository;
import com.basilisk.pipeline_notification_service.repository.NotificationRuleRepository;
import com.basilisk.pipeline_notification_service.repository.PipelineEventRepository;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationRuleRepository notificationRuleRepository;

    @Autowired
    private PipelineEventRepository pipelineEventRepository;


    @BeforeEach
    void cleanDatabase() {

        notificationRepository.deleteAll();
        pipelineEventRepository.deleteAll();
        notificationRuleRepository.deleteAll();
    }


    @Test
    void getAllNotificationsShouldReturnOk() throws Exception {

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk());
    }


    @Test
    void getNotificationByIdShouldReturnNotification() throws Exception {

        // Arrange: create and save the related PipelineEvent
        PipelineEvent event = new PipelineEvent(
                "sales-etl",
                "extract-customers",
                Environment.PROD,
                PipelineStatus.FAILURE,
                "Connection timeout",
                LocalDateTime.now()
        );

        event = pipelineEventRepository.save(event);


        // Arrange: create and save the related NotificationRule
        NotificationRule rule = new NotificationRule(
                "Production failures",
                "sales-etl",
                Environment.PROD,
                PipelineStatus.FAILURE,
                true
        );

        rule = notificationRuleRepository.save(rule);


        // Arrange: create and save the Notification
        Notification notification = new Notification(
                event,
                rule,
                "Pipeline sales-etl has failed"
        );

        notification = notificationRepository.save(notification);


        // Act + Assert
        mockMvc.perform(
                get("/api/notifications/" + notification.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notification.getId()))
                .andExpect(jsonPath("$.message")
                        .value("Pipeline sales-etl has failed"));
    }


    @Test
    void nonExistentNotificationShouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/api/notifications/999999"))
                .andExpect(status().isNotFound());
    }
}