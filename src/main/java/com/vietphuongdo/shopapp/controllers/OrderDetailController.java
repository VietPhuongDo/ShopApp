package com.vietphuongdo.shopapp.controllers;

import com.vietphuongdo.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    @PostMapping
    public ResponseEntity<?> createOrderDetails(@RequestBody @Valid OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok("create orderDetails successfully");
    }

    //id is orderId
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") @Valid Long id) {
        return ResponseEntity.ok("get orderDetails successfully with orderDetailsId: " + id);
    }

    //get a list order details from a order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") @Valid Long orderId) {
        return ResponseEntity.ok("get orderDetails successfully with orderId: " + orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable("id") @Valid Long id, @RequestBody OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok("update orderDetails successfully with orderDetailsId: " + id
                + " orderDetailDTO: " + orderDetailDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetails(@PathVariable("id") @Valid Long id) {
        return ResponseEntity.noContent().build();
    }

}
