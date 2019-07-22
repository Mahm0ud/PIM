package com.commercetools.pim.aggregator.repository;

import com.commercetools.pim.aggregator.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface ProductRepository extends CrudRepository<Product, String> {
    //@Query("SELECT COUNT(p) FROM Product p Where p.createDate = :today")
    long countByCreateDate(Date today);

    //@Query("SELECT COUNT(p) FROM Product p Where p.updateDate = :today")
    long countByUpdateDate(Date today);
}
