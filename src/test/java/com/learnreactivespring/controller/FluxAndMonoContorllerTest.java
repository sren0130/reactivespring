package com.learnreactivespring.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(SpringRunner.class)          // this is for Junit4 testing. Following is for JUnit5.

//  Commented part is also for Junit5.   URL: https://www.baeldung.com/junit-5-runwith
// @ContextConfiguration(classes = { SpringTestConfiguration.class })
@ExtendWith(SpringExtension.class)
@WebFluxTest
public class FluxAndMonoContorllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void flux_approach1() {
       Flux<Integer> integerFlux =  webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();

    }

    @Test
    public void flux_approach2() {
        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .hasSize(4);

//        StepVerifier.create(integerFlux)
//                .expectSubscription()
//                .expectNext(1)
//                .expectNext(2)
//                .expectNext(3)
//                .expectNext(4)
//                .verifyComplete();

    }

    @Test
    public void flux_approach3() {
        List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4);

        EntityExchangeResult<List<Integer>> result = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectedIntegerList, result.getResponseBody());
    }

    @Test
    public void flux_approach4() {
        List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4);

            webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((response)->{
                    assertEquals(expectedIntegerList, response.getResponseBody());
                });
    }

    @Test
    public void fluxStream() {
        Flux<Long> longStreamFlux =  webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longStreamFlux)
                .expectNext(1L)
                .expectNext(2L)
                .thenCancel()
                .verify();
    }

    @Test
    public void mono() {

        Integer expectedValue = new Integer(1);

        webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((response)->{
                    assertEquals(expectedValue, response.getResponseBody());
                });

    }
}
