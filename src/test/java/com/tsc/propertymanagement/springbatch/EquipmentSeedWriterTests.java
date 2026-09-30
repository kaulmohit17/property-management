package com.tsc.propertymanagement.springbatch;

import com.tsc.propertymanagement.constants.enumtype.EquipmentStatus;
import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.domain.EquipmentDetail;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EquipmentSeedWriterTests {
    @Test
    void enrichmentUpdatesOnlyNonblankFieldsAndPreservesMasterIdentity() {
        MongoTemplate mongo = mock(MongoTemplate.class);
        EquipmentSeedWriter writer = new EquipmentSeedWriter(mongo);
        writer.write(List.of(Equipment.builder().tagNumber("TR-01").manufacturer("John Deere")
                .equipmentModel("5220").serialNumber("")
                .equipmentDetail(EquipmentDetail.builder().frontTirePressure("7.50 - 16, 8PR").build()).build()));
        ArgumentCaptor<UpdateDefinition> updates = ArgumentCaptor.forClass(UpdateDefinition.class);
        verify(mongo).upsert(argThat((Query q) -> "TR-01".equals(q.getQueryObject().getString("tagNumber"))),
                updates.capture(), eq(Equipment.class));
        Document update = updates.getValue().getUpdateObject();
        assertThat(update.get("$set", Document.class)).containsEntry("equipmentDetail.frontTirePressure", "7.50 - 16, 8PR")
                .doesNotContainKeys("serialNumber", "equipmentDetail", "_id", "equipmentModel");
        assertThat(update.get("$setOnInsert", Document.class)).containsEntry("equipmentModel", "5220");
    }

    @Test
    void untaggedReleasedMachinesHaveStableDistinctImportTagsOnRetry() {
        MongoTemplate mongo = mock(MongoTemplate.class);
        EquipmentSeedWriter writer = new EquipmentSeedWriter(mongo);
        Equipment first = released("315");
        Equipment retry = released("315");
        Equipment other = released("317");
        writer.write(List.of(first, retry, other));
        assertThat(first.getTagNumber()).startsWith("IMPORT-RELEASED-").isEqualTo(retry.getTagNumber());
        assertThat(other.getTagNumber()).isNotEqualTo(first.getTagNumber());
    }

    private Equipment released(String model) {
        return Equipment.builder().manufacturer("John Deere").equipmentType("SKID STEER LOADER")
                .equipmentModel(model).tagNumber("")
                .equipmentDetail(EquipmentDetail.builder().equipmentStatus(EquipmentStatus.RELEASED).build()).build();
    }
}
