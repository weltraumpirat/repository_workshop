package com.tobiasgoeschel.workshops.repows.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.money.Money;

import java.util.UUID;

@Getter
@EqualsAndHashCode( of = "id" )
@AllArgsConstructor
public class ShoppingCartItem {
    private UUID id;

    private String label;

    private Money price;
}
