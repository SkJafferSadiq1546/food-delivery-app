package com.example.fooddeliverybackend.mapper;

import com.example.fooddeliverybackend.dto.OrderItemDto;
import com.example.fooddeliverybackend.dto.OrderResponseDto;
import com.example.fooddeliverybackend.model.Order;
import com.example.fooddeliverybackend.model.OrderItem;
import java.util.Collections; // ✅ Add this import
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());
        dto.setDeliveredAt(order.getDeliveredAt());
        
        // ✅ CORRECTED: Check for null before processing the items list
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(OrderMapper::toDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(Collections.emptyList()); // Set an empty list if items are null
        }
        
        return dto;
    }

    public static OrderItemDto toDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPricePerItem(orderItem.getPricePerItem());
        
        // This check prevents errors if a menu item was deleted
        if (orderItem.getMenuItem() != null) {
            dto.setMenuItemId(orderItem.getMenuItem().getId());
            dto.setMenuItemName(orderItem.getMenuItem().getName());
        }
        return dto;
        }
}