package com.management.management.repository;

import com.management.management.entity.Book;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, ObjectId> {
    Book findByIsbn(String isbn);
}
