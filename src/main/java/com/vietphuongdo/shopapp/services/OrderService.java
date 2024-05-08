package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.CartItemDTO;
import com.vietphuongdo.shopapp.dtos.OrderDTO;
import com.vietphuongdo.shopapp.entities.*;
import com.vietphuongdo.shopapp.exception.DataNotFoundException;
import com.vietphuongdo.shopapp.repositories.OrderDetailRepository;
import com.vietphuongdo.shopapp.repositories.OrderRepository;
import com.vietphuongdo.shopapp.repositories.ProductRepository;
import com.vietphuongdo.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository; // check if user is existed
    private final OrderDetailRepository orderDetailRepository;

    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public Order  createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id " + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);

        //check shipping date must > today
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Shipping date must be greater than today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));

            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());

            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
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
    @Transactional
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
    @Transactional
    public void deleteOrder(Long orderId) {
        Order optionOrder = orderRepository.findById(orderId).orElse(null);
        //set active = false,not delete
        if(optionOrder != null){
            optionOrder.setActive(false);
            orderRepository.save(optionOrder);
        }
    }
}
