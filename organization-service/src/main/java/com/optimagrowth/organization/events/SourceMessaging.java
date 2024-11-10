package com.optimagrowth.organization.events;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.optimagrowth.organization.events.event.SourceEvent;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
@RequiredArgsConstructor
public class SourceMessaging {

    @Bean
    public Sinks.Many<SourceEvent> producer() {
        return Sinks.many().replay().latest();
    }

    @Bean
    public Supplier<Flux<SourceEvent>> supplier() {
        return () -> producer().asFlux();
    }
}
