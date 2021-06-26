package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void transformUsingMap() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .map(s->s.toUpperCase())
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_length_repeat() {
        Flux<Integer> intFlux = Flux.fromIterable(names)
                .map(s->s.length())
                .repeat(1)
                .log();

        StepVerifier.create(intFlux)
                .expectNext(4,4,4,5,4,4,4,5)
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_filter() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s->s.length() > 4)
                .map(s->s.toUpperCase())
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlagMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E"))         // A, B, C, D, E
                .flatMap(s-> {
                    return Flux.fromIterable(convertToList(s));     // A->[A, newValue], B->[B, newValue]....
                })     // db or external service call that return a flux.
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }

    // FlatMap is most complicated operation!!!! ================><
    @Test
    public void transformUsingFlagMap_usingParallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E"))         // Flux<String>
                .window(2)              // Flux<Flux<string>->(A, B), (C, D), (E), pass 2 elements at a time
                .flatMap((s)->
                        s.map(this::convertToList).subscribeOn(parallel())     // Flux<List<String>
                                .flatMap(ss->Flux.fromIterable(ss)))         // Flux<List<String>
                                                        // db or external service call that return a flux.
                .log();                     // order of elements is not kept, there is a way to keep the order

        StepVerifier.create(stringFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlagMap_usingParallel_keep_order() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E"))         // Flux<String>
                .window(2)              // Flux<Flux<string>->(A, B), (C, D), (E), pass 2 elements at a time
//                .concatMap((s)->
                  .flatMapSequential((s)->
                        s.map(this::convertToList).subscribeOn(parallel())     // Flux<List<String>
                                .flatMap(ss->Flux.fromIterable(ss)))         // Flux<List<String>
                // db or external service call that return a flux.
                .log();                     // order of elements is not kept, there is a way to keep the order

        StepVerifier.create(stringFlux)
                .expectNextCount(10)
                .verifyComplete();
    }
}
