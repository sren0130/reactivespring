package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

// @DataMongoTest
// @ExtendWith(SpringExtension.class)

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {
    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList (new Item(null, "Samsung TV", 400.0),
            new Item(null, "LG TV", 420.0),
            new Item(null,"Apple watch", 299.99),
            new Item(null, "IPhone", 490.0));

    @BeforeTestClass
    public void setup() {
        itemReactiveRepository.deleteAll()
            .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .log()
                .doOnNext((item-> {
                    System.out.println("Inserted item is " + item);
                }))
                .log()
                .blockLast();     // it's only used for test cases.
    }

    @Test
    public void getAllItems() {
            // findAll() return Flux object.
        Flux<Item> itemFlux = itemReactiveRepository.findAll().log();
      //  StepVerifier.create(itemReactiveRepository.findAll())
          StepVerifier.create(itemFlux)
            .expectSubscription()
            .expectNextCount(4)
            .verifyComplete();
    }
}
