package com.virtuslab.internship.discount;

import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import com.virtuslab.internship.product.Product.Type;

import java.math.BigDecimal;

public class GrainDiscount {

    public static String NAME = "GrainDiscount";

    public Receipt apply(Receipt receipt) {
        if (shouldApply(receipt)) {
            var totalPrice = receipt.totalPrice().multiply(BigDecimal.valueOf(0.85));
            var discounts = receipt.discounts();
            discounts.add(NAME);
            return new Receipt(receipt.entries(), discounts, totalPrice);
        }
        return receipt;
    }

    private boolean shouldApply(Receipt receipt) {
        int grainproducts = 0;
        for (ReceiptEntry elem : receipt.entries()) {
            if (elem.product().type() == Type.GRAINS) {
                grainproducts += elem.quantity();
            }
            if (grainproducts >= 3) {
                return true;
            }
        }
        return false;
    }
}
