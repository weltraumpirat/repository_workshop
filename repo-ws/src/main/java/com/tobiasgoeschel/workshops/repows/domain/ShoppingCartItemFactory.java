package com.tobiasgoeschel.workshops.repows.domain;

import static com.tobiasgoeschel.workshops.repows.application.config.MoneyMapper.toMoney;
import com.tobiasgoeschel.workshops.repows.application.persistence.cart.ShoppingCartItemEntity;
import org.joda.money.Money;

import java.util.UUID;

public class ShoppingCartItemFactory {
    public static ShoppingCartItem create( String label, Money price ) {
        return new ShoppingCartItem( UUID.randomUUID(), label, price );
    }

    public static ShoppingCartItem restore( ShoppingCartItemEntity entity ) {
        return new ShoppingCartItem(entity.getId(), entity.getLabel(), toMoney(entity.getPrice()));
    }
}
