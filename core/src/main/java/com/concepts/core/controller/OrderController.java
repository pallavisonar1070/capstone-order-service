package com.concepts.core.controller;

import com.concepts.core.dtos.CreateOrderRequest;
import com.concepts.core.models.Order;
import com.concepts.core.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestHeader("idempotency-key") String idempotencyKey, @RequestBody CreateOrderRequest request){
        Order order = orderService.createOrder(idempotencyKey, request);
        return ResponseEntity.ok(order);
    }
    @GetMapping
    public String test(){
        return "OK";
    }
}
