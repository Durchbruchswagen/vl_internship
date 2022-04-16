package com.virtuslab.internship.endpoint;

import java.util.List;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.GrainDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointController {
    private Basket basket;
    private ProductDb db;

    EndpointController() {
        basket = new Basket();
        db = new ProductDb();
    }

    @GetMapping("/basket")
    List<Product> allproducts() {
        return basket.getProducts();
    }

    @PostMapping("/basket")
    void addproduct(@RequestBody String id) {
        basket.addProduct(db.getProduct(id));

    }

    @DeleteMapping("/basket")
    void removeproduct(@RequestBody String id) {
        basket.removeProduct(db.getProduct(id));
    }

    @GetMapping("/receipt")
    Receipt getReceipt() {
        TenPercentDiscount tenDiscount = new TenPercentDiscount();
        GrainDiscount grainDiscount = new GrainDiscount();
        ReceiptGenerator receiptGenerator = new ReceiptGenerator();
        return tenDiscount.apply(grainDiscount.apply(receiptGenerator.generate(basket, db)));
    }

}
