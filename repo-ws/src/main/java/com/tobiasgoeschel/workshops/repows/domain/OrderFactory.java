package com.tobiasgoeschel.workshops.repows.domain;

import static com.tobiasgoeschel.workshops.repows.application.config.MoneyMapper.toMoney;
import com.tobiasgoeschel.workshops.repows.persistence.order.OrderEntity;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderFactory {
    public static Order create( CurrencyUnit currencyUnit) {
        return new Order( UUID.randomUUID(), Money.zero(currencyUnit), new ArrayList<>() );
    }

    public static Order restore( OrderEntity entity ) {
        final List<OrderPosition> positions = entity.getPositions().stream().map( OrderPositionFactory::restore ).collect(
            Collectors.toList( ));
        return new Order( entity.getId(), toMoney( entity.getTotal() ), positions );
    }
}
