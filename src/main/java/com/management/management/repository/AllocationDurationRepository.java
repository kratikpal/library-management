package com.management.management.repository;

import com.management.management.entity.AllocationDuration;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AllocationDurationRepository extends MongoRepository<AllocationDuration, ObjectId> {
    AllocationDuration findByUserIdAndBookId(ObjectId userId, ObjectId bookId);
}
