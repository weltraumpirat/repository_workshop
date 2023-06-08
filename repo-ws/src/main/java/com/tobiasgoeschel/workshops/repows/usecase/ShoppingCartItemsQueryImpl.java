package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItem;
import com.tobiasgoeschel.workshops.repows.adapter.ShoppingCartItemsQuery;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartEntity;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItemFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ShoppingCartItemsQueryImpl implements ShoppingCartItemsQuery {
    private final ShoppingCartCrudRepository jpaCartsRepo;

    public ShoppingCartItemsQueryImpl( final ShoppingCartCrudRepository jpaCartsRepo ) {
        this.jpaCartsRepo = jpaCartsRepo;
    }

    @Override public List<ShoppingCartItem> invoke( final UUID cartId ) {
        if( jpaCartsRepo.existsById( cartId ) ) {
            return jpaCartsRepo.findById( cartId )
                       .map( ShoppingCartEntity::getItems )
                       .orElse( Collections.emptyList() )
                       .stream()
                       .map( ShoppingCartItemFactory::restore )
                       .toList();
        } else throw shoppingCartNotFoundResponse( cartId );
    }


    private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "A shopping cart with id "+cartId+" does not exist." );
    }
}
