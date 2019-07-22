package com.commercetools.pim.aggregator;

import com.commercetools.pim.aggregator.controller.ProductController;
import com.commercetools.pim.aggregator.model.Product;
import com.commercetools.pim.aggregator.service.ProductService;
import com.commercetools.pim.aggregator.dto.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService service;

    @Test
    public void givenProductObject_thenReturnProductJSON() throws Exception {

        Product iPhone = new Product("7e503ca0-4a22-47a8-b496-63665eacc326", "Apple Mobile", "smart phone", "Apple", true, "PC");

        List<Product> allProducts = Arrays.asList(iPhone);
        given(service.getAllProducts()).willReturn(allProducts);

        mvc.perform(get("/commercetools/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].UUID", is(iPhone.getUUID())));
    }

    @Test
    public void givenStatusObject_thenReturnStatusJSON() throws Exception {

        Status status = new Status(100L,150L,new Date(new java.util.Date().getTime()));
        given(service.getTodayStatus()).willReturn(status);

        mvc.perform(get("/commercetools/status")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("createdProducts", is(status.getCreatedProducts().intValue())))
                .andExpect(jsonPath("updatedProducts", is(status.getUpdatedProducts().intValue())));
    }
}