package com.learnreactivespring.repository;

import com.learnreactivespring.document.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserReactiveRepository extends ReactiveMongoRepository<User, String> {
}
