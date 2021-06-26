package com.learnreactivespring.handler;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @RunWith is Junit4 test, no good, use Junit5 ====== It should use Spring boot test, not Webflux
//
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class SampleHandlerFunctionTest {
    /*
            WebClient and WebTestClient are both for reactive testing, WebTestClient has new
                methods to check message response, isOk, header, content type, body, etc.
     */
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void flux_approach1() {
        Flux<Integer> integerFlux =  webTestClient.get().uri("/fun/flux")
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
                .expectNext(5)
                .expectNext(6)
                .verifyComplete();

    }

    @Test
    public void mono() {

        Integer expectedValue = new Integer(1);

        webTestClient.get().uri("/fun/mono")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((response)->{
                    assertEquals(expectedValue, response.getResponseBody());
                });

    }
}
