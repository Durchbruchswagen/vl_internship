package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrainDiscountTest {
    @Test
    void ApplybothDiscounts() {
        // Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var steak = productDb.getProduct("Steak");
        var cereals = productDb.getProduct("Cereals");
        var bread = productDb.getProduct("Bread");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 2));
        receiptEntries.add(new ReceiptEntry(steak, 3));
        receiptEntries.add(new ReceiptEntry(cereals, 2));
        receiptEntries.add(new ReceiptEntry(bread, 2));

        var cheeseprice = cheese.price().multiply(BigDecimal.valueOf(2));
        var steakprice = steak.price().multiply(BigDecimal.valueOf(3));
        var cerealsprice = cereals.price().multiply(BigDecimal.valueOf(2));
        var breadprice = bread.price().multiply(BigDecimal.valueOf(2));

        var receipt = new Receipt(receiptEntries);
        var tendiscount = new TenPercentDiscount();
        var grainDiscount = new GrainDiscount();
        var expectedTotalPrice = cheeseprice.add(steakprice).add(cerealsprice).add(breadprice)
                .multiply(BigDecimal.valueOf(0.85)).multiply(BigDecimal.valueOf(0.9));

        // When
        var receiptAfterDiscount = tendiscount.apply(grainDiscount.apply(receipt));

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(2, receiptAfterDiscount.discounts().size());
    }

    @Test
    void ApplyOnlyGrainDiscount() {
        // Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var cereals = productDb.getProduct("Cereals");
        var bread = productDb.getProduct("Bread");
        var banana = productDb.getProduct("Banana");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 1));
        receiptEntries.add(new ReceiptEntry(cereals, 1));
        receiptEntries.add(new ReceiptEntry(bread, 2));
        receiptEntries.add(new ReceiptEntry(banana, 4));

        var cheeseprice = cheese.price().multiply(BigDecimal.valueOf(1));
        var cerealsprice = cereals.price().multiply(BigDecimal.valueOf(1));
        var breadprice = bread.price().multiply(BigDecimal.valueOf(2));
        var bananaprice = banana.price().multiply(BigDecimal.valueOf(4));

        var receipt = new Receipt(receiptEntries);
        var tendiscount = new TenPercentDiscount();
        var grainDiscount = new GrainDiscount();
        var expectedTotalPrice = cheeseprice.add(bananaprice).add(cerealsprice).add(breadprice)
                .multiply(BigDecimal.valueOf(0.85));

        // When
        var receiptAfterDiscount = tendiscount.apply(grainDiscount.apply(receipt));

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
    }

    @Test
    void ApplyOnlyTenPercendDiscount() {
        // Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var steak = productDb.getProduct("Steak");
        var cereals = productDb.getProduct("Cereals");
        var bread = productDb.getProduct("Bread");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 2));
        receiptEntries.add(new ReceiptEntry(steak, 3));
        receiptEntries.add(new ReceiptEntry(cereals, 1));
        receiptEntries.add(new ReceiptEntry(bread, 1));

        var cheeseprice = cheese.price().multiply(BigDecimal.valueOf(2));
        var steakprice = steak.price().multiply(BigDecimal.valueOf(3));
        var cerealsprice = cereals.price().multiply(BigDecimal.valueOf(1));
        var breadprice = bread.price().multiply(BigDecimal.valueOf(1));

        var receipt = new Receipt(receiptEntries);
        var tendiscount = new TenPercentDiscount();
        var grainDiscount = new GrainDiscount();
        var expectedTotalPrice = cheeseprice.add(steakprice).add(cerealsprice).add(breadprice)
                .multiply(BigDecimal.valueOf(0.9));

        // When
        var receiptAfterDiscount = tendiscount.apply(grainDiscount.apply(receipt));

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
    }

    @Test
    void NoDiscount() {
        // Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var cereals = productDb.getProduct("Cereals");
        var bread = productDb.getProduct("Bread");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 1));
        receiptEntries.add(new ReceiptEntry(cereals, 1));
        receiptEntries.add(new ReceiptEntry(bread, 1));

        var cheeseprice = cheese.price().multiply(BigDecimal.valueOf(1));
        var cerealsprice = cereals.price().multiply(BigDecimal.valueOf(1));
        var breadprice = bread.price().multiply(BigDecimal.valueOf(1));

        var receipt = new Receipt(receiptEntries);
        var tendiscount = new TenPercentDiscount();
        var grainDiscount = new GrainDiscount();
        var expectedTotalPrice = cheeseprice.add(cerealsprice).add(breadprice);

        // When
        var receiptAfterDiscount = tendiscount.apply(grainDiscount.apply(receipt));

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(0, receiptAfterDiscount.discounts().size());
    }
}
