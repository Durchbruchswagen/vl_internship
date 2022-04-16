package com.virtuslab.internship.webtest;

import com.virtuslab.internship.endpoint.Shopapp;
import com.virtuslab.internship.product.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(classes = Shopapp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestDeleteRequest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DirtiesContext
    @Test
    public void testDeleteRequest() throws Exception {
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Apple", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Apple", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Cheese", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Steak", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Apple", void.class);

        HttpEntity<String> apple = new HttpEntity<String>("Apple");
        HttpEntity<String> steak = new HttpEntity<String>("Steak");

        this.restTemplate.exchange("http://localhost:" + port + "/basket", HttpMethod.DELETE, apple, void.class);
        this.restTemplate.exchange("http://localhost:" + port + "/basket", HttpMethod.DELETE, steak, void.class);

        ResponseEntity<Product[]> returnedvalue = this.restTemplate.getForEntity("http://localhost:" + port + "/basket",
                Product[].class);

        List<Product> products = Arrays.asList(returnedvalue.getBody());

        assertEquals(3, products.size());
    }
}
