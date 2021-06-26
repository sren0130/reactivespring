package com.learnreactivespring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

//@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> returnFlux() {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Integer> returnFluxStream() {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    // it gives infinit data stream, forever, starts from 0, 1, 2,.....
    // close browser, which sends out cancel call to service.
    @GetMapping(value = "/fluxinfinitstream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Long> returnFluxInfinitStream() {
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }

    // When to use mono? when we only want one item returned back.
    @GetMapping("/mono")
    public Mono<Integer> returnMono() {
        return Mono.just(1)
                .log();
    }
}
