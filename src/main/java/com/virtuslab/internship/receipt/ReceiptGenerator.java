package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.product.ProductDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptGenerator {

    public Receipt generate(Basket basket, ProductDb db) {
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        Map<String, Integer> product_quant = new HashMap<>();
        for (Product element : basket.getProducts()) {
            if (product_quant.get(element.name()) == null) {
                product_quant.put(element.name(), 1);
            } else {
                product_quant.put(element.name(), product_quant.get(element.name()) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : product_quant.entrySet()) {
            receiptEntries.add(new ReceiptEntry(db.getProduct(entry.getKey()), entry.getValue().intValue()));
        }
        return new Receipt(receiptEntries);
    }

}
