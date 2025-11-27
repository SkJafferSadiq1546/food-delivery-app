package com.example.fooddeliverybackend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MenuItemDto {
    private String name;
    private BigDecimal price;
    private RestaurantDto restaurant; // Nests the Restaurant DTO
}