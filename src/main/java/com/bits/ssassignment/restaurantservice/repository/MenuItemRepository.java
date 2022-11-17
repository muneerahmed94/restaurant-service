package com.bits.ssassignment.restaurantservice.repository;

import com.bits.ssassignment.restaurantservice.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public
interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findMenuItemsByRestaurantId(Long restaurantId);
}
