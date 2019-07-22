package com.commercetools.pim.aggregator.service;

import com.commercetools.pim.aggregator.dto.Status;
import com.commercetools.pim.aggregator.exception.ResourceNotFoundException;
import com.commercetools.pim.aggregator.model.Product;
import com.commercetools.pim.aggregator.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(String id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public boolean findProduct(String id) {
        return productRepository
                .findById(id).isPresent();
    }

    @Override
    public void save(Product product) {
        Date date = new Date(new java.util.Date().getTime());
        Optional<Product> productOptional = productRepository.findById(product.getUUID());
        if(productOptional.isPresent()){
            product.setCreateDate(productOptional.get().getCreateDate());
            product.setUpdateDate(date);
        }else {
            product.setCreateDate(date);
        }
        productRepository.save(product);
    }

    @Override
    public Status getTodayStatus() {
        Date today = new Date(System.currentTimeMillis());
        long createCount = productRepository.countByCreateDate(today);
        long updateCount = productRepository.countByUpdateDate(today);
        return new Status(createCount, updateCount, today);
    }
}
