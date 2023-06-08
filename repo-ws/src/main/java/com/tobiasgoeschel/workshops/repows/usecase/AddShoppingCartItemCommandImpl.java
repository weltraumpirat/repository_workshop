package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.AddShoppingCartItemCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItem;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;

import java.util.UUID;

public class AddShoppingCartItemCommandImpl implements AddShoppingCartItemCommand {
    private final ShoppingCartRepository repository;

    public AddShoppingCartItemCommandImpl( final ShoppingCartRepository repository ) {
        this.repository = repository;
    }


    @Override public void invoke( final UUID cartId, final ShoppingCartItem item ) {
        final ShoppingCart cart = repository.findById( cartId );
        cart.addItem( item );
    }
}
