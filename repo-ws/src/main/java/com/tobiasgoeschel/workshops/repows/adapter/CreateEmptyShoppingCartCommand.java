package com.tobiasgoeschel.workshops.repows.adapter;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;

public interface CreateEmptyShoppingCartCommand {
    ShoppingCart invoke();
}
