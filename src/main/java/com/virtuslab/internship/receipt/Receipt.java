package com.virtuslab.internship.receipt;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

public record Receipt(
        List<ReceiptEntry> entries,
        List<String> discounts,
        BigDecimal totalPrice) {

    public Receipt(List<ReceiptEntry> entries) {
        this(entries,
                new ArrayList<String>(),
                entries.stream()
                        .map(ReceiptEntry::totalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
