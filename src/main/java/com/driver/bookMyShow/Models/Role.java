package com.driver.bookMyShow.Models;

import com.driver.bookMyShow.Enums.RoleName;

import jakarta.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;


}


