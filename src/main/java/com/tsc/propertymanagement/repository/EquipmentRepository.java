package com.tsc.propertymanagement.repository;

import com.tsc.propertymanagement.domain.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends MongoRepository<Equipment, String> {
    Optional<List<Equipment>> findByTagNumber(String equipmentNumber);
}