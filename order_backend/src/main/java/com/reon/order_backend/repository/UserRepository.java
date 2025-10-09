package com.reon.order_backend.repository;

import com.reon.order_backend.document.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
