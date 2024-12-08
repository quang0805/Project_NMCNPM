package org.project.food_management.controller;

import org.project.food_management.model.Order;
import org.project.food_management.model.OrderItem;
import org.project.food_management.request.CreateOrderItemRequest;
import org.project.food_management.request.CreateOrderRequest;
import org.project.food_management.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(
            @RequestBody CreateOrderRequest req
            ) throws Exception {
        Order order = orderService.createOrder(req);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


    @PostMapping("/order-item")
    public ResponseEntity<OrderItem> createOrderItem(
            @RequestBody CreateOrderItemRequest req
            ) throws Exception {
        OrderItem orderItem = orderService.createOrderItem(req);
        return new ResponseEntity<>(orderItem, HttpStatus.CREATED);
    }
    @DeleteMapping("/order-item/{id}")
    public ResponseEntity<String> deleteOrderItem(
            @PathVariable("id") Long id
    ) throws Exception {
        orderService.deleteOrderItemById(id);
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }

    @GetMapping("/api/admin/get-all-order")
    public ResponseEntity<List<Order>> getAllOrder(){
        List<Order> orders = orderService.getAllOrder();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/get-all-order/{id}")
    public ResponseEntity<List<OrderItem>> getAllOrderItemByOrderId(
            @PathVariable("id") Long id
    ) throws Exception {
        List<OrderItem> orderItems = orderService.getAllOrderItemByOrderId(id);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }


}
