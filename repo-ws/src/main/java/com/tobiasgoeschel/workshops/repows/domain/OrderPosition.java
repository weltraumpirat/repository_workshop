package com.tobiasgoeschel.workshops.repows.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.money.Money;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderPosition {
    private UUID   id;
    private String itemName;
    private int    count;
    private Money  singlePrice;
    private Money combinedPrice;

    public void addOne( final ShoppingCartItem item ) {
        if( item.getLabel().equals( this.itemName ) ) {
            this.combinedPrice = this.combinedPrice.plus( item.getPrice() );
            this.count++;
        }
    }
}
