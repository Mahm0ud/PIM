package com.commercetools.pim.importer.service;

import com.commercetools.pim.importer.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@Service("productService")
public class ProductService {

    private final KafkaTemplate<String, Product> producerTemplate;
    private final String catalogProductsTopic;

    Logger logger = LoggerFactory.getLogger(ProductService.class.getName());

    ProductService(final KafkaTemplate<String, Product> producerTemplate, @Value(value = "${kafka.topic.catalog-products}") String catalogProduct){
        this.producerTemplate = producerTemplate;
        this.catalogProductsTopic = catalogProduct;
    }

    public void transmitProducts(List<Product> products){
        for (Product product: products) {
            sendProduct(catalogProductsTopic,product);
        }
    }

    private void sendProduct(String topic, Product product) {
        ListenableFuture<SendResult<String, Product>> future = producerTemplate.send(topic, product.getName(), product);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Product>>() {
            @Override
            public void onSuccess(SendResult<String, Product> result) {
                logger.info ("Sent message=[" + product + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.info ("Unable to send message=["+ product + "] due to : " + ex.getMessage());
            }
        });
    }
}
