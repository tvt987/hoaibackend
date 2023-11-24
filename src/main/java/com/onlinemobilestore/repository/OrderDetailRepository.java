package com.onlinemobilestore.repository;

import com.onlinemobilestore.entity.Order;
import com.onlinemobilestore.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {


        List<OrderDetail> findAllByOrderId(int user_id);
}
