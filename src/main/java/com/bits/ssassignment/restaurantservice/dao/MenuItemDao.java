package com.bits.ssassignment.restaurantservice.dao;

import com.bits.ssassignment.restaurantservice.entity.MenuItem;
import com.bits.ssassignment.restaurantservice.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuItemDao {
    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getMenuItemsByRestaurantId(Long restaurantId) {
        return menuItemRepository.findMenuItemsByRestaurantId(restaurantId);
    }
}
