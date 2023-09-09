package com.tsc.propertymanagement.repository;

import com.tsc.propertymanagement.domain.Technician;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TechnicianRepository extends MongoRepository<Technician, String> {

}