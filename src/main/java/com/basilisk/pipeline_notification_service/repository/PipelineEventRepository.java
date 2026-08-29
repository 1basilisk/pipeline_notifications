package com.basilisk.pipeline_notification_service.repository;

import com.basilisk.pipeline_notification_service.entity.PipelineEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineEventRepository
        extends JpaRepository<PipelineEvent, Long> {

}
