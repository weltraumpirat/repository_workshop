package com.tobiasgoeschel.workshops.repows.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Order {
    private UUID                id;
    private Money               total;
    private LocalDateTime       timestamp;
    private List<OrderPosition> positions;

    public Order( final UUID id, final Money total, final List<OrderPosition> positions ) {
        this.id = id;
        this.total = total;
        this.positions = positions;
        this.timestamp = LocalDateTime.now();
    }

    public void addItem( final ShoppingCartItem item ) {
        final OrderPosition position = this.positions.stream()
                                           .filter( p -> p.getItemName().equals( item.getLabel() ) )
                                           .findFirst()
                                           .orElse( OrderPositionFactory.create( item.getLabel(), item.getPrice() ) );
        position.addOne(item);
        if(!this.positions.contains( position )) {
            this.positions.add( position );
        }

        this.total = this.positions.stream()
                                .map( OrderPosition::getCombinedPrice )
                                .reduce( Money::plus )
                                .orElse( Money.zero( CurrencyUnit.EUR ) );
    }
}
