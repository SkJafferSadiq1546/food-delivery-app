package com.example.fooddeliverybackend.service;

import com.example.fooddeliverybackend.dto.OrderRequestDto;
import com.example.fooddeliverybackend.model.MenuItem;
import com.example.fooddeliverybackend.model.Order;
import com.example.fooddeliverybackend.model.OrderItem;
import com.example.fooddeliverybackend.repository.MenuItemRepository;
import com.example.fooddeliverybackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private Order updateOrderStatusBasedOnTime(Order order) {
        if ("Delivered".equals(order.getStatus())) {
            return order;
        }

        long minutesPassed = Duration.between(order.getOrderDate(), LocalDateTime.now()).toMinutes();
        String newStatus = order.getStatus();

        if (minutesPassed >= 4) { newStatus = "Delivered"; }
        else if (minutesPassed >= 3) { newStatus = "Out for Delivery"; }
        else if (minutesPassed >= 2) { newStatus = "Preparing Food"; }
        else if (minutesPassed >= 1) { newStatus = "Order Accepted"; }
        
        if (!newStatus.equals(order.getStatus())) {
            order.setStatus(newStatus);
            if ("Delivered".equals(newStatus)) {
                order.setDeliveredAt(LocalDateTime.now());
            }
            return orderRepository.save(order);
        }
        
        return order;
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id).map(this::updateOrderStatusBasedOnTime);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        return orders.stream()
                .map(this::updateOrderStatusBasedOnTime)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<Order> getLatestActiveOrder(Integer userId) {
        return orderRepository.findTopByUserIdAndStatusNotOrderByOrderDateDesc(userId, "Delivered")
                .map(this::updateOrderStatusBasedOnTime);
    }

    public Order createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setUserId(orderRequestDto.getUserId());
        order.setTotalAmount(orderRequestDto.getTotalAmount());
        order.setPaymentMethod(orderRequestDto.getPaymentMethod());
        order.setDeliveryAddress(orderRequestDto.getDeliveryAddress());
        order.setEstimatedDeliveryTime(orderRequestDto.getEstimatedDeliveryTime());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Order Placed");

        List<OrderItem> orderItems = orderRequestDto.getItems().stream().map(itemDto -> {
            MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + itemDto.getMenuItemId()));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPricePerItem(itemDto.getPricePerItem());
            orderItem.setMenuItem(menuItem);
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        return orderRepository.save(order);
    }

    public Optional<Order> updateOrderStatus(Integer id, String status) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isEmpty()) {
            return Optional.empty();
        }

        Order orderToUpdate = orderOptional.get();
        orderToUpdate.setStatus(status);
        if ("Delivered".equals(status)) {
            orderToUpdate.setDeliveredAt(LocalDateTime.now());
        }
        
        Order updatedOrder = orderRepository.save(orderToUpdate);
        return Optional.of(updatedOrder);
    }

    public void deleteOrdersByUserId(Integer userId) {
        orderRepository.deleteByUserId(userId);
    }
}