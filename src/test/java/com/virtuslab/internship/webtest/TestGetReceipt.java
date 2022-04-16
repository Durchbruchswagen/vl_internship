package com.virtuslab.internship.webtest;

import com.virtuslab.internship.endpoint.Shopapp;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
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
public class TestGetReceipt {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DirtiesContext
    @Test
    public void testGetReceipt() {
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Apple", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Apple", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Cheese", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Steak", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Apple", void.class);

        HttpEntity<String> apple = new HttpEntity<String>("Apple");
        HttpEntity<String> steak = new HttpEntity<String>("Steak");

        this.restTemplate.exchange("http://localhost:" + port + "/basket", HttpMethod.DELETE, apple, void.class);
        this.restTemplate.exchange("http://localhost:" + port + "/basket", HttpMethod.DELETE, steak, void.class);

        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Steak", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Steak", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Bread", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Bread", void.class);
        this.restTemplate.postForEntity("http://localhost:" + port + "/basket", "Bread", void.class);

        ResponseEntity<Receipt> returnedvalue = this.restTemplate.getForEntity("http://localhost:" + port + "/receipt",
                Receipt.class);

        var db = new ProductDb();
        var applePrice = (db.getProduct("Apple").price()).multiply(BigDecimal.valueOf(2));
        var cheesePrice = (db.getProduct("Cheese").price()).multiply(BigDecimal.valueOf(1));
        var steakPrice = (db.getProduct("Steak").price()).multiply(BigDecimal.valueOf(2));
        var breadPrice = (db.getProduct("Bread").price()).multiply(BigDecimal.valueOf(3));

        var expectedPrice = applePrice.add(cheesePrice).add(steakPrice).add(breadPrice)
                .multiply(BigDecimal.valueOf(0.85)).multiply(BigDecimal.valueOf(0.9));

        Receipt receipt = returnedvalue.getBody();
        assertEquals(expectedPrice, receipt.totalPrice());
        assertEquals(2, receipt.discounts().size());
        assertEquals(4, receipt.entries().size());
    }
}
