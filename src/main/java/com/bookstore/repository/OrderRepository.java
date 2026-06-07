package com.bookstore.repository;

import com.bookstore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByUserIdOrderByOrderDateDesc(Integer userId, Pageable pageable);

    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate >= :start AND o.orderDate < :end")
    Double getRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT CAST(o.order_date AS DATE) as day, COALESCE(SUM(o.total_amount), 0) as total " +
           "FROM orders o WHERE o.order_date >= :start AND o.order_date < :end " +
           "GROUP BY CAST(o.order_date AS DATE) ORDER BY CAST(o.order_date AS DATE)", nativeQuery = true)
    List<Object[]> getDailyRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
