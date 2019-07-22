package com.commercetools.pim.importer.controller;

import com.commercetools.pim.importer.model.Product;
import com.commercetools.pim.importer.service.ProductService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.concurrent.ListenableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ComponentScan(basePackages = "com.commercetools.pim.importer")
@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductService productService;

    @MockBean
    private KafkaTemplate<String, Product> producerTemplate;

    @MockBean
    ListenableFuture<SendResult<String, Product>> future;

    @Value(value = "${kafka.topic.catalog-products}")
    private String catalogProductsTopic;

    @Test
    public void givenProductObject_thenSendProductObject()throws Exception {
        Product iPhone = new Product("7e503ca0-4a22-47a8-b496-63665eacc326", "Apple Mobile", "smart phone", "Apple", true, "PC");

        Object mock = given(producerTemplate.send(catalogProductsTopic, iPhone.getName(), iPhone)).willReturn(future);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        producerTemplate.send(catalogProductsTopic, iPhone.getName(), iPhone);
        Mockito.verify(producerTemplate).send(eq(catalogProductsTopic), eq(iPhone.getName()), captor.capture());

    }

    @Test
    public void givenCSVObject_thenReturnProductJSON() throws Exception {
        Product htc = new Product("49207e59-a896-4c1b-9ce1-9a03cae06073","HTC Mobile","smart phone","HTC",true,"PC");

        StringBuilder builder = new StringBuilder();
        builder.append("UUID,Name,Description,provider,available,MeasurementUnits\n");
        builder.append("49207e59-a896-4c1b-9ce1-9a03cae06073,HTC Mobile,smart phone,HTC,true,PC");

        MockMultipartFile jsonFile = new MockMultipartFile("test.csv", "", "text/csv", builder.toString().getBytes());

        ProducerRecord producerRecord = new ProducerRecord("catalogProductsTopic","");
        TopicPartition topicPartition = new TopicPartition("catalogProductsTopic",0);
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition,0l,0l,0l,0l,0,0);

        ListenableFuture<SendResult<String, Product>> responseFuture = new AsyncResult<>(new SendResult<>(producerRecord, recordMetadata));

        Object mock = given(producerTemplate.send(any(), any(), any())).willReturn(responseFuture);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);


        mvc.perform(MockMvcRequestBuilders.multipart("/commercetools/upload")
                .file("file", jsonFile.getBytes())
                .characterEncoding("UTF-8"));

        Mockito.verify(producerTemplate).send(eq(catalogProductsTopic), eq(htc.getName()), captor.capture());
        assertThat(captor.getValue()).isEqualToComparingFieldByField(htc);
    }
}