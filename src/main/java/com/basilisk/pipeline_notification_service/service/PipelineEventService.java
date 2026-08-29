package com.basilisk.pipeline_notification_service.service;

import com.basilisk.pipeline_notification_service.entity.PipelineEvent;
import com.basilisk.pipeline_notification_service.repository.PipelineEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PipelineEventService {

    private final PipelineEventRepository repository;

    public PipelineEventService(PipelineEventRepository repository) {
        this.repository = repository;
    }

    public PipelineEvent createEvent(PipelineEvent event) {
        return repository.save(event);
    }

    public Optional<PipelineEvent> getEventById(Long id) {
        return repository.findById(id);
    }

    public List<PipelineEvent> getAllEvents() {
        return repository.findAll();
    }
}