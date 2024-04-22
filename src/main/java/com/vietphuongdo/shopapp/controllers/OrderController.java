package com.vietphuongdo.shopapp.controllers;


import com.vietphuongdo.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @PostMapping("")
    //POST request http://localhost:8088/api/v1/orders
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            return ResponseEntity.ok("Create order successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{user_id}")
    //GET request http://localhost:8088/api/v1/orders/1
    public ResponseEntity<?> getOrder(@Valid @PathVariable("user_id") Long userId) {
        try {
            return ResponseEntity.ok("Get order by id successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    //PUT request http://localhost:8088/api/v1/orders/1
    //admin can do, not personal user
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("id") Long id,
                                         @Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok("Update Order successfully");
    }

    @DeleteMapping("/{id}")
    //DELETE request http://localhost:8088/api/v1/orders/1
    //update active field = false(not deleted in DB)
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Delete Order successfully");
    }


}
