package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.DeleteShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;

import java.util.UUID;

public class DeleteShoppingCartCommandImpl implements DeleteShoppingCartCommand {
    private final ShoppingCartRepository repository;

    public DeleteShoppingCartCommandImpl( final ShoppingCartRepository repository ) {
        this.repository = repository;
    }

    @Override
    public void invoke( UUID cartId) {
        final ShoppingCart cart = repository.findById( cartId );
        repository.remove( cart );
    }
}
