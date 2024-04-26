package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.OrderDTO;
import com.vietphuongdo.shopapp.dtos.OrderDetailDTO;
import com.vietphuongdo.shopapp.entities.OrderDetail;
import com.vietphuongdo.shopapp.exception.DataNotFoundException;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    OrderDetail getOrderDetailById(Long orderDetailId) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long orderDetailId,OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    void deleteOrderDetail(Long orderDetailId) throws DataNotFoundException;
    List<OrderDetail> findByOrderId(Long orderId);
}
