package com.example.fooddeliverybackend.repository;

import com.example.fooddeliverybackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional; // ✅ Add this import

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId);
    void deleteByUserId(Integer userId);
    
    // ✅ NEW: This method finds the most recent order for a user that is NOT delivered
    Optional<Order> findTopByUserIdAndStatusNotOrderByOrderDateDesc(Integer userId, String status);
}