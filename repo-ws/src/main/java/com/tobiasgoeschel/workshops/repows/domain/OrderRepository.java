package com.tobiasgoeschel.workshops.repows.domain;

import java.util.List;

public interface OrderRepository {
    List<Order> findAll();
    void add(Order order);
    void addAll( List<Order> orders );
    void remove(Order order);
    void removeAll(List<Order> orders);
    void removeAll();
}
