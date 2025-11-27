package com.example.fooddeliverybackend.controller;

import com.example.fooddeliverybackend.dto.OrderRequestDto;
import com.example.fooddeliverybackend.dto.OrderResponseDto;
import com.example.fooddeliverybackend.mapper.OrderMapper;
import com.example.fooddeliverybackend.model.Order;
import com.example.fooddeliverybackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000") // <--- ADD THIS
@RestController
@RequestMapping("/")
public class OrderController {
	
    @Autowired
    private OrderService orderService;

    @GetMapping("/user/{userId}/latest-active")
    public ResponseEntity<OrderResponseDto> getLatestActiveOrder(@PathVariable Integer userId) {
        return orderService.getLatestActiveOrder(userId)
                .map(OrderMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id)
                .map(OrderMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable Integer userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderResponseDto> orderDtos = orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }
    
    // ✅ This method is now updated to accept the DTO from the frontend
    @PostMapping("/orders") 
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        Order savedOrder = orderService.createOrder(orderRequestDto);
        return ResponseEntity.ok(OrderMapper.toDto(savedOrder));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Integer id, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        return orderService.updateOrderStatus(id, status)
                .map(OrderMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteOrdersByUserId(@PathVariable Integer userId) {
        orderService.deleteOrdersByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}