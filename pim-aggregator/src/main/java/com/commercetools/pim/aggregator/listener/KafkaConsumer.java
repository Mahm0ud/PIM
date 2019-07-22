package com.commercetools.pim.aggregator.listener;


import com.commercetools.pim.aggregator.model.Product;
import com.commercetools.pim.aggregator.service.ProductService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumer {

    private ProductService productService;

    KafkaConsumer(final ProductService productService){
        this.productService = productService;
    }

    @KafkaListener(topics = "${kafka.topic.catalog-products}", groupId = "${zookeeper.groupId}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(Product product, @Header(KafkaHeaders.OFFSET) List<Long> offsets){
        System.out.println(product);
        productService.save(product);
    }
}
