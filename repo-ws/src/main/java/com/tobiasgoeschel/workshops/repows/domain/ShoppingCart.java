package com.tobiasgoeschel.workshops.repows.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode( of = "id" )
@AllArgsConstructor
public class ShoppingCart {
    private UUID                   id;
    private List<ShoppingCartItem> items;

    public void addItem( final ShoppingCartItem item ) {
        this.items.add( item );
    }

    public void removeItem( final UUID itemId ) {
        this.items.stream()
            .filter( i -> i.getId().equals( itemId ) )
            .findFirst()
            .or( ()-> {
                throw shoppingCartItemNotFoundResponse( this.id, itemId );
            })
            .ifPresent( i -> this.items.remove( i ) );

    }

    private ResponseStatusException shoppingCartItemNotFoundResponse( final UUID cartId, final UUID cartItemId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "An item with id "+cartItemId+" does not exist in shopping cart with id "+cartId+"." );
    }
}
