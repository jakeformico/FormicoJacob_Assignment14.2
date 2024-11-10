package com.optimagrowth.organization.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.organization.events.event.SourceEvent;
import com.optimagrowth.organization.events.model.OrganizationChangeModel;
import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import com.optimagrowth.organization.utils.UserContext;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    private final OrganizationRepository repository;

    private final Sinks.Many<SourceEvent> producer;

    private final ObservationRegistry observationRegistry;

    private final Tracer tracer;

    public Organization findById(String organizationId) {
        Optional<Organization> opt = repository.findById(organizationId);
        this.publishOrganizationChange("GET", organizationId);
        Observation.createNotStarted("getOrg", this.observationRegistry).observe(() -> {
            logger.debug("Got organization ID: {}", organizationId);
            logger.debug("Trace ID: {}", this.tracer.currentSpan().context().traceId());
        });
        return (opt.isPresent()) ? opt.get() : null;
    }

    public Organization create(final Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        repository.save(organization);
        this.publishOrganizationChange("SAVE", organization.getId());
        return organization;
    }

    public void update(Organization organization) {
        repository.save(organization);
        this.publishOrganizationChange("UPDATE", organization.getId());
    }

    public void delete(String organizationId) {
        repository.deleteById(organizationId);
        this.publishOrganizationChange("DELETE", organizationId);
    }

    @SuppressWarnings("unused")
    private void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    public void publishOrganizationChange(String action, String organizationId) {
        logger.debug("Sending Kafka message {} for Organization Id: {}", action, organizationId);
        final OrganizationChangeModel change = new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                organizationId,
                UserContext.getCorrelationId());
        var sourceEvent = new SourceEvent.OrganizationCreated(change);
        producer.tryEmitNext(sourceEvent);
    }
}