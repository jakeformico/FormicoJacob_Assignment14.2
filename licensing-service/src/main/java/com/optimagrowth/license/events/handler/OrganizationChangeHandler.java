package com.optimagrowth.license.events.handler;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.optimagrowth.license.events.event.SourceEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrganizationChangeHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationChangeHandler.class);

    @Bean
    public Consumer<SourceEvent.OrganizationCreated> handleOrganizationCreated() {
        return this::handle;
    }

    private void handle(SourceEvent.OrganizationCreated organizationCreated) {

        logger.debug("Received a message of type " + organizationCreated);

    }

}
