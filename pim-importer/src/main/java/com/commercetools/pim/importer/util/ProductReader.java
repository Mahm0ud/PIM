package com.commercetools.pim.importer.util;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class ProductReader {

    private static final CsvMapper mapper = new CsvMapper();
    public static <T> List<T> read(Class<T> clazz, InputStream stream) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder().setSkipFirstDataRow(true)
                .addColumn("UUID")
                .addColumn("Name")
                .addColumn("Description")
                .addColumn("provider")
                .addColumn("available", CsvSchema.ColumnType.BOOLEAN)
                .addColumn("MeasurementUnits")
                .build();
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        return reader.<T>readValues(stream).readAll();
    }

}
