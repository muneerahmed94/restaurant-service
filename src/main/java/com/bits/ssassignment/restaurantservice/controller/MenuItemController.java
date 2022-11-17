package com.bits.ssassignment.restaurantservice.controller;

import com.bits.ssassignment.restaurantservice.dao.MenuItemDao;
import com.bits.ssassignment.restaurantservice.entity.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenuItemController {
    @Autowired
    private MenuItemDao menuItemDao;

    @RequestMapping(value="/menu/{restaurantId}", method = RequestMethod.GET)
    @ResponseBody
    public List<MenuItem> getMenuItemsByRestaurantId(@PathVariable Long restaurantId) {
        return menuItemDao.getMenuItemsByRestaurantId(restaurantId);
    }
}
