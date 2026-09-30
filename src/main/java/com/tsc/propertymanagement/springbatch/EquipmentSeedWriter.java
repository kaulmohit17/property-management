package com.tsc.propertymanagement.springbatch;

import com.tsc.propertymanagement.constants.enumtype.EquipmentStatus;
import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.domain.EquipmentDetail;
import org.bson.Document;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/** Merges CSV fragments by tag, without replacing the equipment ID or other details. */
@Component
public class EquipmentSeedWriter implements ItemWriter<Equipment> {
    private final MongoTemplate mongo;

    public EquipmentSeedWriter(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public void write(List<? extends Equipment> items) {
        for (Equipment equipment : items) {
            EquipmentDetail detail = equipment.getEquipmentDetail();
            if (!StringUtils.hasText(equipment.getTagNumber())) {
                if (detail != null && detail.getEquipmentStatus() == EquipmentStatus.RELEASED) {
                    equipment.setTagNumber("IMPORT-RELEASED-" + identity(equipment));
                } else {
                    Document document = new Document();
                    mongo.getConverter().write(equipment, document);
                    document.put("_id", "unassigned-key-" + identity(equipment));
                    mongo.save(document, "unassignedEquipmentKeys");
                    continue;
                }
            }
            Update update = new Update()
                    .setOnInsert("tagNumber", equipment.getTagNumber());
            // The master equipment list is loaded first and owns these fields.
            insertText(update, "equipmentType", equipment.getEquipmentType());
            insertText(update, "manufacturer", equipment.getManufacturer());
            insertText(update, "equipmentModel", equipment.getEquipmentModel());
            setText(update, "serialNumber", equipment.getSerialNumber());
            if (detail != null) {
                setText(update, "equipmentDetail.equipmentRemarks", detail.getEquipmentRemarks());
                setText(update, "equipmentDetail.frontTirePressure", detail.getFrontTirePressure());
                setText(update, "equipmentDetail.backTirePressure", detail.getBackTirePressure());
                if (detail.getKeyDetail() != null) {
                    setText(update, "equipmentDetail.keyDetail.primaryKey", detail.getKeyDetail().getPrimaryKey());
                }
            }
            if (detail != null && detail.getEquipmentStatus() == EquipmentStatus.RELEASED) {
                update.set("equipmentDetail.equipmentStatus", EquipmentStatus.RELEASED);
            } else {
                update.setOnInsert("equipmentDetail.equipmentStatus", EquipmentStatus.IN_SERVICE);
            }
            mongo.upsert(Query.query(Criteria.where("tagNumber").is(equipment.getTagNumber())), update, Equipment.class);
        }
    }

    private static void insertText(Update update, String field, String value) {
        if (StringUtils.hasText(value)) update.setOnInsert(field, value);
    }

    private static void setText(Update update, String field, String value) {
        if (StringUtils.hasText(value)) update.set(field, value);
    }

    private static String identity(Equipment equipment) {
        // Length-safe serialization avoids collisions between concatenated source fields.
        Document source = new Document("type", equipment.getEquipmentType())
                .append("manufacturer", equipment.getManufacturer())
                .append("model", equipment.getEquipmentModel())
                .append("serial", equipment.getSerialNumber());
        EquipmentDetail detail = equipment.getEquipmentDetail();
        if (detail != null) {
            source.append("remarks", detail.getEquipmentRemarks());
            if (detail.getKeyDetail() != null) source.append("key", detail.getKeyDetail().getPrimaryKey());
        }
        return UUID.nameUUIDFromBytes(source.toJson().getBytes(StandardCharsets.UTF_8)).toString();
    }
}
