package com.bits.ssassignment.restaurantservice.repository;

import com.bits.ssassignment.restaurantservice.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public
interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
