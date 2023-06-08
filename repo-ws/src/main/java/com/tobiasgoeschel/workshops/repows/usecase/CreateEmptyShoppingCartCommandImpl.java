package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.CreateEmptyShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartEntity;

public class CreateEmptyShoppingCartCommandImpl implements CreateEmptyShoppingCartCommand {
    private final ShoppingCartCrudRepository repository;

    public CreateEmptyShoppingCartCommandImpl( final ShoppingCartCrudRepository crudRepository ) {
        this.repository = crudRepository;
    }

    @Override public ShoppingCart invoke() {
        final ShoppingCart cart = ShoppingCartFactory.create();
        repository.save( ShoppingCartEntity.fromCart( cart ) );
        return cart;
    }
}
