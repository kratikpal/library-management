package com.management.management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class Book {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String isbn;
    @NonNull
    private String title;
    @NonNull
    private String author;
    @NonNull
    private int quantity;
}
