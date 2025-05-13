package com.management.management.entity;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "allocationDuration")
public class AllocationDuration {
    @Id
    private ObjectId id;

    @NonNull
    private ObjectId userId;
    @NonNull
    private ObjectId bookId;
    @NonNull
    private LocalDate issueDate;
    @NonNull
    private LocalDate dueDate;
    private boolean returned = false;
    private boolean isNotified = false;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
