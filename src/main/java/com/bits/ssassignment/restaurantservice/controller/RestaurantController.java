package com.bits.ssassignment.restaurantservice.controller;

import com.bits.ssassignment.restaurantservice.dao.RestaurantDao;
import com.bits.ssassignment.restaurantservice.entity.MenuItem;
import com.bits.ssassignment.restaurantservice.entity.Restaurant;
import com.bits.ssassignment.restaurantservice.model.KafkaMessage;
import com.bits.ssassignment.restaurantservice.model.Notification;
import com.bits.ssassignment.restaurantservice.model.Order;
import com.bits.ssassignment.restaurantservice.repository.MenuItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller("/")
public class RestaurantController {
    @Autowired
    private RestaurantDao restaurantDao;

    @RequestMapping(value="/restaurants", method = RequestMethod.GET)
    @ResponseBody
    public List<Restaurant> getRestaurants() {
        return restaurantDao.getRestaurants();
    }

    @RequestMapping(value="/restaurants/{restaurantId}", method = RequestMethod.GET)
    @ResponseBody
    public Restaurant getRestaurantById(@PathVariable Long restaurantId) {
        return restaurantDao.getRestaurantById(restaurantId);
    }

    @RequestMapping(value="/listenOrder", method = RequestMethod.POST)
    @ResponseBody
    public KafkaMessage listenOrder(@RequestBody KafkaMessage message) {
        Order order = getOrderFromString(message.getContent());

        Restaurant restaurant = getRestaurantById(order.getRestaurantId());

        notifyRestaurant(restaurant, message);

        return message;
    }

    @RequestMapping(value="/order/{orderId}", method = RequestMethod.POST)
    @ResponseBody
    public Order updateOrder(@PathVariable Long orderId, @RequestBody Order order) {
        order.setId(orderId);
        publishOrderToKafka(order);
        return order;
    }

    private void notifyRestaurant(Restaurant restaurant, KafkaMessage message) {
        Notification notification = new Notification();
        notification.setEmail(restaurant.getEmail());
        notification.setSubject("New Order received");
        notification.setBody(message.getContent());

        callNotificationService(notification);
    }

    private void callNotificationService(Notification notification) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/notify";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Notification> request = new HttpEntity<>(notification, headers);

        restTemplate.postForEntity( url, request , Notification.class);
    }


    private Order getOrderFromString(String orderString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(orderString, Order.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void publishOrderToKafka(Order order) {
        String url = "http://localhost:8084/kafka";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("topic", "OrderUpdates");
        rootNode.put("content", convertObjectToString(order));

        HttpEntity<String> request = new HttpEntity<>(rootNode.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        System.out.println(response);
    }

    public String convertObjectToString(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //Converting the Object to JSONString
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
