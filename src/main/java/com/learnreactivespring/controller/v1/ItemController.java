package com.learnreactivespring.controller.v1;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.document.User;
import com.learnreactivespring.repository.ItemReactiveRepository;
import com.learnreactivespring.repository.UserReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Autowired
    UserReactiveRepository userReactiveRepository;

    @GetMapping("/v1/all")
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }

    @GetMapping("/v1/{id}")
    public Mono<User> getAllItems(@PathVariable("id") String id) {
        return userReactiveRepository.findById(id);
    }
}
