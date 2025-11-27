package com.example.fooddeliverybackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;
    private Double rating;
}