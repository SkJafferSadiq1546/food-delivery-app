package com.example.fooddeliverybackend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Integer id;
    private Integer userId;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private String status;
    private String deliveryAddress;
    private String paymentMethod;
    private String estimatedDeliveryTime;
    private LocalDateTime deliveredAt;
    private List<OrderItemDto> items;
}