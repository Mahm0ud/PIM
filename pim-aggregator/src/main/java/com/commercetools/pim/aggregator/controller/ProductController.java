package com.commercetools.pim.aggregator.controller;

import com.commercetools.pim.aggregator.dto.Status;
import com.commercetools.pim.aggregator.model.Product;
import com.commercetools.pim.aggregator.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/commercetools")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/products")
    public @NotNull Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(value = "/status")
    public Status getStats() {
        return productService.getTodayStatus();
    }
}
