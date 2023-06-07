package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.CreateEmptyShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartEntity;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartItemEntity;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;

import java.util.List;

public class CreateEmptyShoppingCartCommandImpl implements CreateEmptyShoppingCartCommand {
    private final ShoppingCartCrudRepository repository;

    public CreateEmptyShoppingCartCommandImpl( final ShoppingCartCrudRepository crudRepository ) {
        this.repository = crudRepository;
    }

    @Override public ShoppingCart invoke() {
        final ShoppingCart cart = ShoppingCartFactory.create();
        final List<ShoppingCartItemEntity> items = cart.getItems().stream()
                                                       .map( item -> new ShoppingCartItemEntity( item.getId(),
                                                                                                 item.getLabel(),
                                                                                                 item.getPrice()
                                                                                                     .toString() ) )
                                                       .toList();
        final ShoppingCartEntity entity = new ShoppingCartEntity( cart.getId(), items );
        repository.save( entity );
        return cart;
    }
}
