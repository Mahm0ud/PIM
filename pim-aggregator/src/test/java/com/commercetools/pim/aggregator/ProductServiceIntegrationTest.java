package com.commercetools.pim.aggregator;

import com.commercetools.pim.aggregator.model.Product;
import com.commercetools.pim.aggregator.repository.ProductRepository;
import com.commercetools.pim.aggregator.service.ProductService;
import com.commercetools.pim.aggregator.service.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ProductServiceIntegrationTest {
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private Optional<Product> existingProductOptional;

    private ProductService productService;


    @Before
    public void setUp() {
        productService = new ProductServiceImpl(productRepository);
        Product sonyMobile = new Product("39f8a349-f359-446f-a0fd-60d72f810ce0","Sony  Mobile","smart phone","Sony",true,"PC");
        sonyMobile.setUUID("11");

        Product lgMobile = new Product("88d458f4-6443-4f03-96f3-0bc23d481025","LG Mobile","smart phone","LG",true,"PC");
        Product motorolaMobile = new Product("2dcaf00d-0fec-445d-a2f9-c66b6c59da0a","Motorola Mobile","smart phone","Motorola",true,"PC");

        List<Product> allProducts = Arrays.asList(sonyMobile, lgMobile, motorolaMobile);


        Mockito.when(productRepository.findById(sonyMobile.getUUID())).thenReturn(Optional.of(sonyMobile));
        Mockito.when(productRepository.findAll()).thenReturn(allProducts);
        Mockito.when(productRepository.findById("-99")).thenReturn(Optional.empty());
    }

    @Test
    public void whenValidId_thenProductShouldBeFound() {
        Product fromDb = productService.getProduct("11");
        assertThat(fromDb.getName()).isEqualTo("Sony  Mobile");

        verifyFindByIdIsCalledOnce();
    }

    @Test
    public void whenInValidId_thenProductShouldNotBeFound() {
        boolean fromDb = productService.findProduct("-99");
        verifyFindByIdIsCalledOnce();
        assertThat(fromDb).isFalse();
    }

    @Test
    public void given1Product_whenProductCreated_thenCreateDateIsFilledAndUpdateDateIsNull(){
        Product motorolaMobile = new Product("2dcaf00d-0fec-445d-a2f9-c66b6c59da0a","Motorola Mobile","smart phone","Motorola",true,"PC");

        Mockito.when(existingProductOptional.isPresent()).thenReturn(false);
        Mockito.when(existingProductOptional.get()).thenReturn(motorolaMobile);
        Mockito.when(productRepository.findById(motorolaMobile.getUUID())).thenReturn(existingProductOptional);

        Mockito.when(productRepository.existsById(motorolaMobile.getUUID())).thenReturn(false);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        productService.save(motorolaMobile);

        Mockito.verify(productRepository).save(captor.capture());

        assertThat(captor.getValue().getCreateDate()).isEqualToIgnoringHours(new java.sql.Date(System.currentTimeMillis()));
        assertThat(captor.getValue().getUpdateDate()).isNull();
    }

    @Test
    public void given1Product_whenProductUpdated_UpdatedDateIsFilled(){
        Product sonyMobile = new Product("39f8a349-f359-446f-a0fd-60d72f810ce0","Sony  Mobile","smart phone","Sony",true,"PC");

        long dayInMillis = TimeUnit.DAYS.toMillis(1);
        Date createDate = new Date(System.currentTimeMillis()-dayInMillis);
        Date updateDate = new java.sql.Date(System.currentTimeMillis());

        sonyMobile.setCreateDate(createDate);

        Mockito.when(existingProductOptional.isPresent()).thenReturn(true);
        Mockito.when(existingProductOptional.get()).thenReturn(sonyMobile);
        Mockito.when(productRepository.findById(sonyMobile.getUUID())).thenReturn(existingProductOptional);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        productService.save(sonyMobile);
        Mockito.verify(productRepository).save(captor.capture());

        assertThat(captor.getValue().getUpdateDate()).isEqualToIgnoringHours(updateDate);
        assertThat(captor.getValue().getCreateDate()).isEqualToIgnoringHours(createDate);
    }


    @Test
    public void given3Products_whenGetAll_thenReturn3Records() {
        Product sonyMobile = new Product("39f8a349-f359-446f-a0fd-60d72f810ce0","Sony  Mobile","smart phone","Sony",true,"PC");
        Product lgMobile = new Product("88d458f4-6443-4f03-96f3-0bc23d481025","LG Mobile","smart phone","LG",true,"PC");
        Product motorolaMobile = new Product("2dcaf00d-0fec-445d-a2f9-c66b6c59da0a","Motorola Mobile","smart phone","Motorola",true,"PC");


        Iterable<Product> allProducts = productService.getAllProducts();
        verifyFindAllEmployeesIsCalledOnce();
        assertThat(allProducts).hasSize(3).extracting(Product::getName).contains(sonyMobile.getName(), lgMobile.getName(), motorolaMobile.getName());
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(productRepository, VerificationModeFactory.times(1)).findById(Mockito.anyString());
        Mockito.reset(productRepository);
    }

    private void verifyFindAllEmployeesIsCalledOnce() {
        Mockito.verify(productRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(productRepository);
    }
}
