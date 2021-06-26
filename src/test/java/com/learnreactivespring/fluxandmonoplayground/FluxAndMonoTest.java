package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")     // flux has data to emit
//                .concatWith(Flux.error(new RuntimeException(("Exception occuring!"))))      // add exception.
                .concatWith(Flux.just("After error"))
                .log();

        stringFlux
                .subscribe(System.out::println,                             //  attach to the flux to get value from it.
                        (e)->System.out.println(e.getMessage()));           // add exception handling.


                    // subscribe has many overloaded methods.
    }

    // Got some strange error: INFO: 0 containers and 3 tests were Method or class mismatch
    //     then run gradle clean and build, no good, then run clean, bootBuildImage, then
    //     test, clean, build, test finally shows good results, no INFO red warning thing.
    @Test
    public void fluxTestElements_withoutError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();                  // without this call, terminal operation, no results
    }

    @Test
    public void fluxTestElements_withError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurring")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
//                .expectError(RuntimeException.class)                  // you can't do both, Error and ErrorMessage
                .expectErrorMessage("Exception occurring")
                .verify();
    }

    @Test
    public void fluxTestElementCount() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void fluxTestElements_withError2() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurring")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .expectErrorMessage("Exception occurring")
                .verify();
    }

    @Test
    public void monoTest() {

        Mono<String> stringMono = Mono.just("Spring Mono").log();

        StepVerifier.create(stringMono)
                .expectNext("Spring Mono")
                .verifyComplete();

    }

    @Test
    public void monoTest_Error() {

        Mono<String> stringMono = Mono.just("Spring Mono").log();

        StepVerifier.create(Mono.error(new RuntimeException("Exception occurring")))
                .expectError(RuntimeException.class)
                .verify();

    }
}
