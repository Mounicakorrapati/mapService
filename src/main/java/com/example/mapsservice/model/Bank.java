package com.example.mapsservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bank {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
}
