package com.tobiasgoeschel.workshops.repows.domain;

import static com.tobiasgoeschel.workshops.repows.application.config.MoneyMapper.toMoney;
import com.tobiasgoeschel.workshops.repows.application.persistence.order.OrderPositionEntity;
import org.joda.money.Money;

import java.util.UUID;

public class OrderPositionFactory {
    public static OrderPosition create( String name, Money singlePrice) {
        return new OrderPosition( UUID.randomUUID(), name, 0, singlePrice, Money.zero( singlePrice.getCurrencyUnit() ));
    }

    public static OrderPosition restore( OrderPositionEntity entity ) {
        return new OrderPosition( entity.getId(), entity.getItemName(), entity.getCount(), toMoney(entity.getSinglePrice()), toMoney(entity.getCombinedPrice()) );
    }
}
