package com.example.mapsservice.service;

import com.example.mapsservice.model.Bank;
import com.example.mapsservice.model.Coordinates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class GoogleMapsService {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GoogleMapsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

        public List<Bank> getBanksNearZipcode(String zipcode) {
        Coordinates coords = getCoordinatesFromZipcode(zipcode);
        return findNearbyBanks(coords.getLatitude(), coords.getLongitude());
    }

    private List<Bank> findNearbyBanks(double latitude, double longitude) {
        String url = String.format(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=16093&type=bank&key=%s",
                latitude, longitude, apiKey);

        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);

        List<Bank> banks = new ArrayList<>();
        JSONArray results = json.getJSONArray("results");

        // Parse the response and convert it into a list of Bank objects
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String name = result.getString("name");
            String address = result.getString("vicinity");
            JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");

            banks.add(new Bank(name, address, lat, lng));
        }

        return banks;
    }
    /**
     * Fetches nearby banks based on the provided zipcode.
     *
     * @param zipcode The zipcode to search for nearby banks.
     * @return A list of banks near the specified zipcode.
     */


    public Coordinates getCoordinatesFromZipcode(String zipcode) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                zipcode + "&key=" + apiKey;

        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);

        if (!"OK".equals(json.getString("status"))) {
            throw new RuntimeException("Error fetching coordinates for zipcode: " + zipcode);
        }

        JSONArray results = json.getJSONArray("results");
        JSONObject location = results.getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        double lat = location.getDouble("lat");
        double lng = location.getDouble("lng");

        return new Coordinates(lat, lng);
    }
}
