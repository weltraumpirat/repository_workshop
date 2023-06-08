package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.ShoppingCartItemsQuery;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItem;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;

import java.util.List;
import java.util.UUID;

public class ShoppingCartItemsQueryImpl implements ShoppingCartItemsQuery {
    private final ShoppingCartRepository repository;

    public ShoppingCartItemsQueryImpl( final ShoppingCartRepository repository ) {
        this.repository = repository;
    }

    @Override public List<ShoppingCartItem> invoke( final UUID cartId ) {
        return repository.findById( cartId ).getItems();
    }
}
