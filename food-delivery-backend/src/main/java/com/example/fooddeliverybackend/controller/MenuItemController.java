package com.example.fooddeliverybackend.controller;

import com.example.fooddeliverybackend.model.MenuItem;
import com.example.fooddeliverybackend.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class MenuItemController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @GetMapping("/menu-items")
    public List<Map<String, Object>> getAllMenuItems() {
        return menuItemRepository.findAll().stream().map(menuItem -> {
            Map<String, Object> menuItemMap = new java.util.HashMap<>();
            menuItemMap.put("id", menuItem.getId());
            menuItemMap.put("name", menuItem.getName());
            menuItemMap.put("price", menuItem.getPrice());
            menuItemMap.put("category", menuItem.getCategory());
            menuItemMap.put("image", menuItem.getImageUrl());
            if (menuItem.getRestaurant() != null) {
                menuItemMap.put("restaurant", menuItem.getRestaurant().getName());
                menuItemMap.put("address", menuItem.getRestaurant().getAddress());
                menuItemMap.put("rating", menuItem.getRestaurant().getRating());
            }
            return menuItemMap;
        }).collect(Collectors.toList());
    }

    // ✅ This is the updated endpoint to get a single item by its ID
    @GetMapping("/menu-items/{id}")
    public ResponseEntity<Map<String, Object>> getMenuItemById(@PathVariable Integer id) {
        Optional<MenuItem> menuItemOptional = menuItemRepository.findById(id);

        if (menuItemOptional.isPresent()) {
            MenuItem menuItem = menuItemOptional.get();
            Map<String, Object> menuItemMap = new java.util.HashMap<>();
            menuItemMap.put("id", menuItem.getId());
            menuItemMap.put("name", menuItem.getName());
            menuItemMap.put("price", menuItem.getPrice());
            menuItemMap.put("category", menuItem.getCategory());
            menuItemMap.put("image", menuItem.getImageUrl());
            if (menuItem.getRestaurant() != null) {
                menuItemMap.put("restaurant", menuItem.getRestaurant().getName());
                menuItemMap.put("address", menuItem.getRestaurant().getAddress());
                menuItemMap.put("rating", menuItem.getRestaurant().getRating());
            }
            return ResponseEntity.ok(menuItemMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}