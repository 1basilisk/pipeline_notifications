package com.basilisk.pipeline_notification_service.repository;

import com.basilisk.pipeline_notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

}