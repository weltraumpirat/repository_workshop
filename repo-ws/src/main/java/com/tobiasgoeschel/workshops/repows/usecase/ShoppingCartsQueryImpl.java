package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.ShoppingCartsQuery;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;

import java.util.List;

public class ShoppingCartsQueryImpl implements ShoppingCartsQuery {
    private final ShoppingCartRepository repository;

    public ShoppingCartsQueryImpl( ShoppingCartRepository repository ) {
        this.repository = repository;
    }

    @Override public List<ShoppingCart> invoke() {
        return repository.findAll();

    }
}
