package com.tsc.propertymanagement.springbatch;

import org.bson.Document;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/** Workshop stock has quantities, not machine tags; preserve each source row separately. */
@Component
public class EquipmentQuantityImport {
    private final MongoTemplate mongo;

    public EquipmentQuantityImport(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    public void run() throws Exception {
        FlatFileItemReader<FieldSet> reader = reader();
        try {
            reader.open(new ExecutionContext());
            FieldSet row;
            int line = 1;
            while ((row = reader.read()) != null) {
                int quantity = row.readInt("quantity");
                if (quantity < 0) throw new IllegalArgumentException("Negative inventory quantity");
                mongo.save(new Document("_id", "equipment-quantities.csv:" + ++line)
                        .append("description", row.readString("description"))
                        .append("quantity", quantity)
                        .append("source", "equipment-quantities.csv"), "equipmentInventory");
            }
        } finally {
            reader.close();
        }
    }

    public static FlatFileItemReader<FieldSet> reader() {
        return new FlatFileItemReaderBuilder<FieldSet>()
                .name("equipmentQuantityReader")
                .resource(new ClassPathResource("equipment-quantities.csv"))
                .linesToSkip(1)
                .delimited().names("description", "quantity")
                .fieldSetMapper(fieldSet -> fieldSet)
                .build();
    }
}
