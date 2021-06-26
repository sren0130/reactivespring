package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void filterTest() {

        Flux<String> nameFlux = Flux.fromIterable(names)
                .filter(s->s.startsWith("a"))
                .log();         // only adam, anna are returned.

        StepVerifier.create(nameFlux)
                .expectNext("adam", "anna")
                .verifyComplete();

    }

    @Test
    public void filterByLengthTest() {

        Flux<String> nameFlux = Flux.fromIterable(names)
                .filter(s->s.length() > 4)
                .log();         // only adam, anna are returned.

        StepVerifier.create(nameFlux)
                .expectNext("jenny")
                .verifyComplete();

    }
}
