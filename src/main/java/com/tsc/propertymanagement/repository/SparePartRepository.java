package com.tsc.propertymanagement.repository;

import com.tsc.propertymanagement.domain.Part;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SparePartRepository extends MongoRepository<Part, String> {
}