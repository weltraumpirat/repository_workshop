package com.tobiasgoeschel.workshops.repows.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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
}
