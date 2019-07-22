package com.commercetools.pim.aggregator.service;

import com.commercetools.pim.aggregator.dto.Status;
import com.commercetools.pim.aggregator.model.Product;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface ProductService {

    @NotNull Iterable<Product> getAllProducts();

    Product getProduct(String id);

    boolean findProduct(String id);

    void save(Product product);

    Status getTodayStatus();
}
