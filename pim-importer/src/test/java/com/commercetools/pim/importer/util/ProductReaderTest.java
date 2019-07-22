package com.commercetools.pim.importer.util;

import com.commercetools.pim.importer.model.Product;
import org.assertj.core.api.Fail;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductReaderTest {

    @Test
    public void read() {
        String productId = "49207e59-a896-4c1b-9ce1-9a03cae06073";
        StringBuilder builder = new StringBuilder();
        builder.append("UUID,Name,Description,provider,available,MeasurementUnits\n");
        builder.append(productId+",HTC Mobile,smart phone,HTC,true,PC");
        byte[] fileBytes = builder.toString().getBytes();
        InputStream input = new ByteArrayInputStream(fileBytes);
        try {
            List<Product> products = ProductReader.read(Product.class, input);
            assertThat(products.size()==1).isTrue();
            assertThat(products.get(0).getUUID()).isEqualTo(productId);
        } catch (IOException e) {
            Fail.fail("IO Exception happened", e);
        }
    }
}