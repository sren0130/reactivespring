package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge() {

        // two microservice calls, the returns are two flux
        Flux<String> flux1 = Flux.just("A", "D", "C");
        Flux<String> flux2 = Flux.just("B", "E", "F");

        Flux<String> mergeFlux = Flux.merge(flux1, flux2)
                .log();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("A", "D", "C","B", "E", "F")
                .verifyComplete();

    }

    @Test
    public void combineUsingMerge_withDelay() {

        // two microservice calls, the returns are two flux
        // with delay, no order is kept.
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergeFlux = Flux.merge(flux1, flux2)
                .log("log method");


        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void combineUsingConcat() {

        // two microservice calls, the returns are two flux
        // with delay, no order is kept.
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergeFlux = Flux.concat(flux1, flux2)
                .log();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();

    }
    @Test
    public void combineUsingConcat_withDelay() {

        // two microservice calls, the returns are two flux
        // with delay, no order is kept.
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergeFlux = Flux.concat(flux1, flux2)
                .log();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void combineUsingZip() {

        // two microservice calls, the returns are two flux
        // with delay, no order is kept.
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergeFlux = Flux.zip(flux1, flux2, (t1, t2)->      // A, D : B, E: C, F
        {
            return t1.concat(t2);           // AD, BE, CF
        })
                .log();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }
}
