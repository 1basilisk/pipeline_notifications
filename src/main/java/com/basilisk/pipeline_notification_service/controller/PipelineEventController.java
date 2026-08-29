package com.basilisk.pipeline_notification_service.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.basilisk.pipeline_notification_service.dto.CreatePipelineEventRequest;
import com.basilisk.pipeline_notification_service.entity.PipelineEvent;
import com.basilisk.pipeline_notification_service.service.PipelineEventService;

import jakarta.validation.Valid;
@RestController
public class PipelineEventController {

    private final PipelineEventService service;

    public PipelineEventController(PipelineEventService service) {
        this.service = service;
    }

    @PostMapping("/api/events")
    public PipelineEvent createEvent(
            @Valid @RequestBody CreatePipelineEventRequest request) {

        PipelineEvent event = new PipelineEvent(
                request.getPipelineName(),
                request.getJobName(),
                request.getEnvironment(),
                request.getStatus(),
                request.getErrorMessage(),
                request.getOccurredAt()
        );

        return service.createEvent(event);
    }

    @GetMapping("/api/events/{id}")
    public ResponseEntity<PipelineEvent> getEvent(@PathVariable Long id) {

    Optional<PipelineEvent> event = service.getEventById(id);

    if (event.isPresent()) {
        return ResponseEntity.ok(event.get());
    }

    return ResponseEntity.notFound().build();
    
    }

    @GetMapping("/api/events")
    public List<PipelineEvent> getAllEvents() {
        return service.getAllEvents();
    }


}