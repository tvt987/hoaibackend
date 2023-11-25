package com.onlinemobilestore.service.serviceImpl;

import com.onlinemobilestore.dto.ProductInOrderDTO;
import com.onlinemobilestore.entity.*;
import com.onlinemobilestore.repository.OrderDetailRepository;
import com.onlinemobilestore.repository.OrderRepository;
import com.onlinemobilestore.repository.ProductRepository;
import com.onlinemobilestore.repository.UserRepository;
import com.onlinemobilestore.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public ProductInOrderDTO createOrderAndOrderDetailByUserIdAndByProductId(int userId, int productId) {
        try {
            Order order = new Order();
            User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

            order.setUser(user);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(1);

            ProductInOrderDTO productInOrderDTO = new ProductInOrderDTO();
            productInOrderDTO.setNameProduct(product.getName());
            productInOrderDTO.setProductId(productId);
            productInOrderDTO.setPrice(product.getPrice() * (1 - (product.getPercentDiscount()/ 100.0)));
            List<String> imageUrl = product.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList());
            productInOrderDTO.setImages(imageUrl);
            productInOrderDTO.setCreateDate(new Date());
            orderRepository.save(order);
            orderDetailRepository.save(orderDetail);


            return productInOrderDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error creating order and order detail", e);
        }
    }

}
