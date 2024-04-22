package com.vietphuongdo.shopapp.repositories;

import com.vietphuongdo.shopapp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
