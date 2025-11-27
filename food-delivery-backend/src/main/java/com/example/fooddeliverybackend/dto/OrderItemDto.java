package com.example.fooddeliverybackend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Integer id;
    private int quantity;
    private BigDecimal pricePerItem;
    
    // ✅ ADD THESE FIELDS
    private Integer menuItemId;
    private String menuItemName;
}