package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.RemoveShoppingCartItemCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;

import java.util.UUID;

public class RemoveShoppingCartItemCommandImpl implements RemoveShoppingCartItemCommand {

    private final ShoppingCartRepository repository;

    public RemoveShoppingCartItemCommandImpl( final ShoppingCartRepository repository ) {
        this.repository = repository;
    }

    @Override public void invoke( final UUID cartId, final UUID itemId ) {
        final ShoppingCart cart = repository.findById( cartId );
        cart.removeItem( itemId );
    }
}
