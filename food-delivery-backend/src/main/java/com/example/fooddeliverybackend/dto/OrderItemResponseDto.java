package com.example.fooddeliverybackend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private int quantity;
    private BigDecimal pricePerItem;
    private MenuItemDto menuItem; // Nests the MenuItem DTO
}