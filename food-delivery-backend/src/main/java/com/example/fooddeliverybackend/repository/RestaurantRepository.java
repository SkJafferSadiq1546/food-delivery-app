package com.example.fooddeliverybackend.repository;

import com.example.fooddeliverybackend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}