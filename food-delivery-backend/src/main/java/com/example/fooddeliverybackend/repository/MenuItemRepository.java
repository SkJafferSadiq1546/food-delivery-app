package com.example.fooddeliverybackend.repository;

import com.example.fooddeliverybackend.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}