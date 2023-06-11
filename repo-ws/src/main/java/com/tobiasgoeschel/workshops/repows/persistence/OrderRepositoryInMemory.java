package com.tobiasgoeschel.workshops.repows.persistence;

import com.tobiasgoeschel.workshops.repows.domain.Order;
import com.tobiasgoeschel.workshops.repows.domain.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OrderRepositoryInMemory implements OrderRepository {
    private final Set<Order> orders;

    public OrderRepositoryInMemory() {
        this.orders = new HashSet<>();
    }

    @Override
    public List<Order> findAll() {
        return orders.stream().toList();
    }

    @Override
    public void add(Order order) {
        if (orders.contains(order)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An order with id " + order.getId() + " already exists.");
        } else {
            orders.add(order);
        }
    }

    @Override
    public void addAll(List<Order> orders) {
        orders.forEach(this::add);
    }

    @Override
    public void remove(Order order) {
        if (!orders.contains(order)) {
            throw orderNotFoundResponse(order.getId());
        } else {
            orders.remove(order);
        }
    }

    @Override
    public void removeAll(List<Order> orders) {
        orders.forEach(this::remove);
    }

    @Override
    public void removeAll() {
        orders.clear();
    }

    private ResponseStatusException orderNotFoundResponse(final UUID orderId) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                "An order with id " + orderId + " does not exist.");
    }
}
