package com.learnreactivespring.initialize;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class ItemDataInitializer {// implements CommandLineRunner {
//    @Autowired
//    ItemReactiveRepository itemReactiveRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        intialDataSetup();
//    }
//
//    public List<Item> data() {
//        return Arrays.asList(new Item(null, "Samsung TV", 399.99),
//                new Item(null, "LG TV", 329.99),
//                new Item(null, "Apple Watch", 349.99),
//                new Item("ABC", "Beats HeadPhones", 19.99));
//    }
//
//    private void intialDataSetup() {
//        itemReactiveRepository.deleteAll()
//                .thenMany(Flux.fromIterable(data()))
//                    .flatMap(itemReactiveRepository::save)
//                    .thenMany(itemReactiveRepository.findAll())
//                    .subscribe((item->{
//                        System.out.println("Item inserted from CommandLineRunner: " + item);
//                    }));
//    }
}
