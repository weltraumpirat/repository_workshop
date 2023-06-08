package com.tobiasgoeschel.workshops.repows.domain;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartRepository {
    ShoppingCart findById( UUID id );
    void add(ShoppingCart cart);
    void addAll( List<ShoppingCart> carts);
    void remove(ShoppingCart cart);
    void removeAll( List<ShoppingCart> carts);

    List<ShoppingCart> findAll();
}
