package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.CreateEmptyShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;

public class CreateEmptyShoppingCartCommandImpl implements CreateEmptyShoppingCartCommand {
    private final ShoppingCartRepository repository;

    public CreateEmptyShoppingCartCommandImpl( final ShoppingCartRepository repository ) {
        this.repository = repository;
    }

    @Override public ShoppingCart invoke() {
        final ShoppingCart cart = ShoppingCartFactory.create();
        repository.add( cart );
        return cart;
    }
}
