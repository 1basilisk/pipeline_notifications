package com.basilisk.pipeline_notification_service.repository;

import com.basilisk.pipeline_notification_service.entity.NotificationRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRuleRepository
        extends JpaRepository<NotificationRule, Long> {

    List<NotificationRule> findByEnabledTrue();
}