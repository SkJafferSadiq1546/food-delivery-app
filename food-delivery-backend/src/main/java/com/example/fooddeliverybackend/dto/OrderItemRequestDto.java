package com.example.fooddeliverybackend.dto;

import lombok.Data;
import java.math.BigDecimal; // ✅ Make sure this import is present

@Data
public class OrderItemRequestDto {
    private Integer menuItemId;
    private int quantity;
    private BigDecimal pricePerItem; // ✅ CORRECTED this line
}