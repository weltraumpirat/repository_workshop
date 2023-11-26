package com.tobiasgoeschel.workshops.repows.lambda.persistence;

import com.tobiasgoeschel.workshops.repows.domain.Order;
import com.tobiasgoeschel.workshops.repows.domain.OrderRepository;

import java.util.List;
/** This class is for demonstration only - persistence on AWS is not part of the workshop **/
public class OrderRepositoryDynamoDB implements OrderRepository {
    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public void add(Order order) {

    }

    @Override
    public void addAll(List<Order> orders) {

    }

    @Override
    public void remove(Order order) {

    }

    @Override
    public void removeAll(List<Order> orders) {

    }

    @Override
    public void removeAll() {

    }
}
