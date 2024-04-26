package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.OrderDTO;
import com.vietphuongdo.shopapp.entities.Order;
import com.vietphuongdo.shopapp.exception.DataNotFoundException;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long orderId) throws DataNotFoundException;
    List<Order> findByUserId(Long userId);
    Order updateOrder(Long orderId, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long orderId);
}
