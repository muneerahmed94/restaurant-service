package com.bits.ssassignment.restaurantservice.dao;

import com.bits.ssassignment.restaurantservice.entity.Restaurant;
import com.bits.ssassignment.restaurantservice.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantDao {
    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).get();
    }
}
