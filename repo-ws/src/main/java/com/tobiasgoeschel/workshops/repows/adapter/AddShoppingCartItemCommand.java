package com.tobiasgoeschel.workshops.repows.adapter;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItem;

import java.util.UUID;

public interface AddShoppingCartItemCommand {
    void invoke( UUID cartId, ShoppingCartItem item );
}
