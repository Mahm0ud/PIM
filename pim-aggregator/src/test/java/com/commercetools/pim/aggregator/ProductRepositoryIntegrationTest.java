package com.commercetools.pim.aggregator;

import com.commercetools.pim.aggregator.model.Product;
import com.commercetools.pim.aggregator.repository.ProductRepository;
import com.commercetools.pim.aggregator.service.ProductService;
import com.commercetools.pim.aggregator.service.ProductServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindById_thenReturnProduct() {
        Product iPhone = new Product("7e503ca0-4a22-47a8-b496-63665eacc326", "Apple Mobile", "smart phone", "Apple", true, "PC");
        entityManager.persistAndFlush(iPhone);

        Product found = productRepository.findById(iPhone.getUUID()).get();
        assertThat(found.getName()).isEqualTo(found.getName());
    }


    @Test
    public void whenInvalidName_thenReturnNull() {
        Product fromDb = productRepository.findById("doesNotExist").orElse(null);
        assertThat(fromDb).isNull();
    }


    @Test
    public void givenSetOfProducts_whenFindAll_thenReturnAllProducts() {
        Product sonyMobile = new Product("39f8a349-f359-446f-a0fd-60d72f810ce0","Sony  Mobile","smart phone","Sony",true,"PC");
        Product lgMobile = new Product("88d458f4-6443-4f03-96f3-0bc23d481025","LG Mobile","smart phone","LG",true,"PC");
        Product motorolaMobile = new Product("2dcaf00d-0fec-445d-a2f9-c66b6c59da0a","Motorola Mobile","smart phone","Motorola",true,"PC");

        entityManager.persist(sonyMobile);
        entityManager.persist(lgMobile);
        entityManager.persist(motorolaMobile);
        entityManager.flush();

        Iterable<Product> products = productRepository.findAll();

        assertThat(products).hasSize(3).extracting(Product::getName).containsOnly(sonyMobile.getName(), lgMobile.getName(), motorolaMobile.getName());
    }

    @Test
    public void given2Products_when2Created1Updated_thenStatusHas2Created1Updated(){
        Product sonyMobile = new Product("39f8a349-f359-446f-a0fd-60d72f810ce0","Sony  Mobile","smart phone","Sony",true,"PC");
        Product motorolaMobile = new Product("2dcaf00d-0fec-445d-a2f9-c66b6c59da0a","Motorola Mobile","smart phone","Motorola",true,"PC");

        ProductService service = new ProductServiceImpl(productRepository);
        service.save(sonyMobile);

        service.save(motorolaMobile);
        motorolaMobile.setName("Updated");
        service.save(motorolaMobile);

        assertThat(productRepository.countByCreateDate(new java.sql.Date(System.currentTimeMillis()))).isEqualTo(2);
        assertThat(productRepository.countByUpdateDate(new java.sql.Date(System.currentTimeMillis()))).isEqualTo(1);
    }
}