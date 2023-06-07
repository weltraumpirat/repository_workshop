package com.tobiasgoeschel.workshops.repows.adapter;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItem;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartItemsQuery {
    List<ShoppingCartItem> invoke( UUID cartId );
}
