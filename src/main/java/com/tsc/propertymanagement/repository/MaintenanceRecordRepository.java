package com.tsc.propertymanagement.repository;

import com.tsc.propertymanagement.domain.MaintenanceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends MongoRepository<MaintenanceRecord, String> {
    List<MaintenanceRecord> findByEquipmentId(String equipmentId);
}