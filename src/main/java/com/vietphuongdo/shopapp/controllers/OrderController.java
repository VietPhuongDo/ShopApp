package com.vietphuongdo.shopapp.controllers;


import com.vietphuongdo.shopapp.components.LocalizationUtils;
import com.vietphuongdo.shopapp.dtos.OrderDTO;
import com.vietphuongdo.shopapp.entities.Order;
import com.vietphuongdo.shopapp.responses.OrderResponse;
import com.vietphuongdo.shopapp.services.OrderService;
import com.vietphuongdo.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    //POST request http://localhost:8088/api/v1/orders
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Order orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/user/{user_id}")
    //GET request http://localhost:8088/api/v1/orders/user/6
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {
            List<Order> orderList =orderService.findByUserId(userId);
            return ResponseEntity.ok(orderList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    //GET request http://localhost:8088/api/v1/order/1
    public ResponseEntity<?> getOrder(@Valid @PathVariable("orderId") Long orderId) {
        try {
            Order existingOrder = orderService.getOrder(orderId);
            return ResponseEntity.ok(OrderResponse.fromOrder(existingOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}")
    //PUT request http://localhost:8088/api/v1/orders/1
    //admin can do, not personal user
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("orderId") Long orderId,
                                         @Valid @RequestBody OrderDTO orderDTO) {
        try {
            Order updatedOrder = orderService.updateOrder(orderId,orderDTO);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{orderId}")
    //DELETE request http://localhost:8088/api/v1/orders/1
    //update active field = false(not deleted in DB)
    public ResponseEntity<String> deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ORDER_SUCCESSFULLY));
    }


}
