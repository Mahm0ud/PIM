package com.commercetools.pim.importer.controller;

import com.commercetools.pim.importer.model.Product;
import com.commercetools.pim.importer.service.ProductService;
import com.commercetools.pim.importer.util.ProductReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/commercetools")
public class ProductController {
    Logger logger = LoggerFactory.getLogger(ProductController.class.getName());

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/upload", consumes = "text/csv")
    public int uploadSimple(@RequestBody InputStream body) {
        int returnSize=0;
        try {
            List<Product> products = ProductReader.read(Product.class, body);
            productService.transmitProducts(products);
            returnSize = products.size();
        } catch (IOException e) {
            logger.error("Error while reading Products input file", e);
        }
        return returnSize;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public int uploadMultipart(@RequestParam("file") MultipartFile file) {
        int returnSize=0;
        try {
            List<Product> products = ProductReader.read(Product.class, file.getInputStream());
            productService.transmitProducts(products);
            returnSize = products.size();
        } catch (IOException e) {
            logger.error("Error while reading Products input file", e);
        }
        return returnSize;
    }

}