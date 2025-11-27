package com.example.fooddeliverybackend.dto;

import lombok.Data;
import java.math.BigDecimal; // ✅ Make sure this import is present
import java.util.List;

@Data
public class OrderRequestDto {
    private Integer userId;
    private BigDecimal totalAmount; // ✅ This must be BigDecimal
    private String paymentMethod;
    private String deliveryAddress;
    private String estimatedDeliveryTime;
    private List<OrderItemRequestDto> items;
}