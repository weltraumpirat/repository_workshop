package com.tobiasgoeschel.workshops.repows.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tobiasgoeschel.workshops.repows.application.config.MoneyMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;
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

    public static class MoneyDeserializer extends StdDeserializer<Money> {
      public MoneyDeserializer() {
        this(Money.class);
      }
      public MoneyDeserializer( final Class<Money> vc ) {
        super( vc );
      }

      @Override public Money deserialize( final JsonParser p, final DeserializationContext ctxt )
          throws IOException {
        JsonNode node = p.getCodec().readTree( p );
        final String text = node.asText();
        return MoneyMapper.toMoney( text );
      }

    }
}
