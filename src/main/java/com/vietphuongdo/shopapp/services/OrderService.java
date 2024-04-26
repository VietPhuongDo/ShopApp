package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.OrderDTO;
import com.vietphuongdo.shopapp.entities.Order;
import com.vietphuongdo.shopapp.entities.OrderStatus;
import com.vietphuongdo.shopapp.entities.User;
import com.vietphuongdo.shopapp.exception.DataNotFoundException;
import com.vietphuongdo.shopapp.repositories.OrderRepository;
import com.vietphuongdo.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository; // check if user is existed
    private final ModelMapper modelMapper;
    @Override
    public Order  createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id "+orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapping -> mapping.skip(Order::setId));

        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus. PENDING);

        //check shipping date must > today
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Shipping date must be greater than today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrder(Long orderId) throws DataNotFoundException {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new DataNotFoundException("Order not found with id "+orderId)
        );
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order updateOrder(Long orderId, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new DataNotFoundException("Order not found with id "+orderId)
        );
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(
                () -> new DataNotFoundException("User not found with id "+orderId)
        );

        //mapping orderDTO to Order skip orderId
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO,existingOrder);
        existingOrder.setUser(existingUser);
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order optionOrder = orderRepository.findById(orderId).orElse(null);
        //set active = false,not delete
        if(optionOrder != null){
            optionOrder.setActive(false);
            orderRepository.save(optionOrder);
        }

    }
}
