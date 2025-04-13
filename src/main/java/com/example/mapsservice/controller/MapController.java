package com.example.mapsservice.controller;

import com.example.mapsservice.model.Bank;
import com.example.mapsservice.model.Coordinates;
import com.example.mapsservice.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maps")
public class MapController {

    @Autowired
    private GoogleMapsService googleMapsService;

    @GetMapping
    public List<Bank> getBanksByZipcode(@RequestParam String zipcode) {
        return googleMapsService.getBanksNearZipcode(zipcode);  // 10-mile radius
    }
    @GetMapping("/coords")
public Coordinates getCoords(@RequestParam String zipcode) {
    return googleMapsService.getCoordinatesFromZipcode(zipcode);
}


}
