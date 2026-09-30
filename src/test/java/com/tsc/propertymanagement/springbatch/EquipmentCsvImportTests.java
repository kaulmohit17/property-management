package com.tsc.propertymanagement.springbatch;

import com.tsc.propertymanagement.domain.Equipment;
import com.tsc.propertymanagement.springbatch.equipmentprocessor.*;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "app.seed.enabled=false")
class EquipmentCsvImportTests {
    @Autowired EquipmentInformationProcessor information;
    @Autowired EquipmentKeyDetailsProcessor keys;
    @Autowired EquipmentTechnicalDetailsProcessor technical;
    @Autowired ReleasedEquipmentProcessor released;

    @Test
    void readsEveryMasterEquipmentAndHandlesEof() throws Exception {
        List<Equipment> rows = readAll(information);
        assertThat(rows).hasSize(96);
        assertThat(rows).extracting(Equipment::getTagNumber).doesNotHaveDuplicates().doesNotContain("");
        assertThat(find(rows, "LM-02").getManufacturer()).isEqualTo("Toro 21\", Recycler");
        assertThat(find(rows, "LS-02").getEquipmentType()).isEqualTo("WALK BEHIND LAWN SPREADER");
    }

    @Test
    void readsKeysIncludingUnassignedKeyAndRemarks() throws Exception {
        List<Equipment> rows = readAll(keys);
        assertThat(rows).hasSize(24);
        assertThat(find(rows, "TR-04").getEquipmentDetail().getKeyDetail().getPrimaryKey()).isEqualTo("22 / (Spare 31)");
        Equipment unassigned = find(rows, "");
        assertThat(unassigned.getEquipmentDetail().getKeyDetail().getPrimaryKey()).isEqualTo("47");
        assertThat(unassigned.getEquipmentDetail().getEquipmentRemarks()).isEqualTo("Equipment Tools - Technicians");
    }

    @Test
    void readsQuotedTireSpecificationsWithoutSplittingEmbeddedCommas() throws Exception {
        List<Equipment> rows = readAll(technical);
        assertThat(rows).hasSize(95);
        Equipment tractor = find(rows, "TR-01");
        assertThat(tractor.getSerialNumber()).isEqualTo("LV5220S422169");
        assertThat(tractor.getEquipmentDetail().getFrontTirePressure()).isEqualTo("7.50 - 16, 8PR");
        assertThat(tractor.getEquipmentDetail().getBackTirePressure()).isEqualTo("16.9 - 28, 6PR");
        assertThat(find(rows, "BL-02").getEquipmentModel()).isNull();
        assertThat(find(rows, "LB-01").getEquipmentDetail().getEquipmentRemarks()).isEqualTo("Oct 21");
    }

    @Test
    void readsAllReleasedEquipmentIncludingMissingTags() throws Exception {
        List<Equipment> rows = readAll(released);
        assertThat(rows).hasSize(25);
        assertThat(rows.stream().filter(e -> e.getTagNumber().isEmpty()).count()).isEqualTo(15);
        assertThat(find(rows, "TR-02").getEquipmentDetail().getEquipmentRemarks()).isEqualTo("Taken back by owner");
    }

    @Test
    void inventoryHeaderIsNotImportedAndRepeatedDescriptionsArePreserved() throws Exception {
        FlatFileItemReader<FieldSet> reader = EquipmentQuantityImport.reader();
        try {
            reader.open(new ExecutionContext());
            int count = 0;
            int quantity = 0;
            int wrenches = 0;
            FieldSet row;
            while ((row = reader.read()) != null) {
                count++;
                quantity += row.readInt("quantity");
                if (row.readString("description").equals("Filter wrench")) wrenches++;
            }
            assertThat(count).isEqualTo(17);
            assertThat(quantity).isEqualTo(19);
            assertThat(wrenches).isEqualTo(3);
        } finally {
            reader.close();
        }
    }

    private List<Equipment> readAll(ItemReader<Equipment> reader) throws Exception {
        ItemStream stream = (ItemStream) reader;
        try {
            stream.open(new ExecutionContext());
            List<Equipment> rows = new ArrayList<>();
            Equipment row;
            while ((row = reader.read()) != null) rows.add(row);
            assertThat(reader.read()).isNull();
            return rows;
        } finally {
            stream.close();
        }
    }

    private Equipment find(List<Equipment> rows, String tag) {
        return rows.stream().filter(e -> e.getTagNumber().equals(tag)).findFirst().orElseThrow();
    }
}
