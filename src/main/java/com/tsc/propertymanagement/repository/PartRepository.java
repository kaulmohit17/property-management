package com.tsc.propertymanagement.repository;

import com.tsc.propertymanagement.domain.Part;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends MongoRepository<Part, String> {
}