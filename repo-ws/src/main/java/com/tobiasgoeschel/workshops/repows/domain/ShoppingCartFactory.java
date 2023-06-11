package com.tobiasgoeschel.workshops.repows.domain;

import com.tobiasgoeschel.workshops.repows.application.persistence.cart.ShoppingCartEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShoppingCartFactory {
    public static ShoppingCart create() {
        return new ShoppingCart( UUID.randomUUID(), new ArrayList<>() );
    }

    public static ShoppingCart restore( ShoppingCartEntity entity ) {
        final List<ShoppingCartItem> items = entity.getItems().stream()
                                                 .map( ShoppingCartItemFactory::restore )
                                                 .collect( Collectors.toList( ));
        return new ShoppingCart( entity.getId(), items );
    }
}

