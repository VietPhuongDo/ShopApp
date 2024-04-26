package com.vietphuongdo.shopapp.controllers;

import com.vietphuongdo.shopapp.dtos.OrderDetailDTO;
import com.vietphuongdo.shopapp.entities.OrderDetail;
import com.vietphuongdo.shopapp.exception.DataNotFoundException;
import com.vietphuongdo.shopapp.responses.OrderDetailResponse;
import com.vietphuongdo.shopapp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<?> createOrderDetails(@RequestBody @Valid OrderDetailDTO orderDetailDTO) {
        try {
             OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.OrderDetailMapper(newOrderDetail));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //id is orderId
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") @Valid Long id) throws DataNotFoundException {
       OrderDetail orderDetail = orderDetailService.getOrderDetailById(id);
       return ResponseEntity.ok().body(OrderDetailResponse.OrderDetailMapper(orderDetail));
    }

    //get a list order details from a order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") @Valid Long orderId) {
        List<OrderDetail> orderDetailList =  orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponseList = orderDetailList.stream().map(
                OrderDetailResponse::OrderDetailMapper).toList();
        return ResponseEntity.ok(orderDetailResponseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable("id") @Valid Long id, @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id,orderDetailDTO);
            return ResponseEntity.ok(orderDetail);
        } catch (DataNotFoundException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetails(@PathVariable("id") @Valid Long id) {
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok().body("Delete Order Details with id "+id+ " successfully");
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
