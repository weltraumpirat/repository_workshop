package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.RemoveShoppingCartItemCommand;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartEntity;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartItemEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

public class RemoveShoppingCartItemCommandImpl implements RemoveShoppingCartItemCommand {

    private final ShoppingCartCrudRepository jpaCartRepository;

    public RemoveShoppingCartItemCommandImpl( final ShoppingCartCrudRepository jpaCartRepository ) {
        this.jpaCartRepository = jpaCartRepository;
    }

    @Override public void invoke( final UUID cartId, final UUID itemId ) {
        if( jpaCartRepository.existsById( cartId )) {
            final ShoppingCartEntity cart = jpaCartRepository.findById( cartId ).orElseThrow();
            final List<ShoppingCartItemEntity> items = cart.getItems();
            items.stream()
                .filter( i -> i.getId().equals( itemId ) )
                .findFirst()
                .or( ()-> {
                    throw shoppingCartItemNotFoundResponse( cartId, itemId );
                })
                .ifPresent( (i)->{
                    cart.getItems().remove( i );
                    jpaCartRepository.save( cart );
                } );
        } else throw shoppingCartNotFoundResponse( cartId );
    }

    private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "A shopping cart with id "+cartId+" does not exist." );
    }
    private ResponseStatusException shoppingCartItemNotFoundResponse( final UUID cartId, final UUID cartItemId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "An item with id "+cartItemId+" does not exist in shopping cart with id "+cartId+"." );
    }
}
