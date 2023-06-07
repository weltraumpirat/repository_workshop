package com.tobiasgoeschel.workshops.repows.adapter;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;

import java.util.List;

public interface ShoppingCartsQuery {
    List<ShoppingCart> invoke();
}
