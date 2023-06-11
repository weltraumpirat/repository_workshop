package com.tobiasgoeschel.workshops.repows.lambda.persistence;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;

import java.util.List;
import java.util.UUID;

/** This class is for demonstration only - persistence on AWS is not part of the workshop **/
public class ShoppingCartRepositoryDynamoDB implements ShoppingCartRepository {
    @Override
    public ShoppingCart findById(UUID id) {
        return null;
    }

    @Override
    public void add(ShoppingCart cart) {

    }

    @Override
    public void addAll(List<ShoppingCart> carts) {

    }

    @Override
    public void remove(ShoppingCart cart) {

    }

    @Override
    public void removeAll(List<ShoppingCart> carts) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public List<ShoppingCart> findAll() {
        return null;
    }
}
