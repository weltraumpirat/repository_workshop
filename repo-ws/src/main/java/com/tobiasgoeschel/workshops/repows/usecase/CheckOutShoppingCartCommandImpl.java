package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.application.config.MoneyMapper;
import static com.tobiasgoeschel.workshops.repows.application.config.MoneyMapper.toMoney;
import com.tobiasgoeschel.workshops.repows.adapter.CheckOutShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartEntity;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartItemEntity;
import com.tobiasgoeschel.workshops.repows.persistence.order.OrderCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.order.OrderEntity;
import com.tobiasgoeschel.workshops.repows.persistence.order.OrderPositionEntity;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CheckOutShoppingCartCommandImpl implements CheckOutShoppingCartCommand {
    private final ShoppingCartCrudRepository jpaCartsRepo;
    private final OrderCrudRepository        jpaOrdersRepo;


    public CheckOutShoppingCartCommandImpl( final ShoppingCartCrudRepository jpaCartsRepo,
                                            final OrderCrudRepository jpaOrdersRepo ) {
        this.jpaCartsRepo = jpaCartsRepo;
        this.jpaOrdersRepo = jpaOrdersRepo;
    }

    @Override public UUID invoke( final UUID cartId ) {
        if( jpaCartsRepo.existsById( cartId ) ) {
            final ShoppingCartEntity cart = jpaCartsRepo.findById( cartId ).orElseThrow();
            final List<ShoppingCartItemEntity> items = cart.getItems();
            if( items.size()>0 ) {
                final Map<String, OrderPositionEntity> positions = new HashMap<>();
                items.forEach( ( item -> addOne( item, positions ) ) );

                final List<OrderPositionEntity> sortedPositions =
                    positions.values().stream()
                        .sorted( ( o1, o2 ) -> o1.getItemName().compareToIgnoreCase( o2.getItemName() ) )
                        .toList();

                final Money total = sortedPositions.stream()
                                        .map( OrderPositionEntity::getCombinedPrice )
                                        .map( MoneyMapper::toMoney )
                                        .reduce( Money::plus )
                                        .orElse( Money.zero( CurrencyUnit.EUR ) );


                final OrderEntity order = new OrderEntity( UUID.randomUUID(), total.toString(), sortedPositions );
                jpaOrdersRepo.save( order );
                jpaCartsRepo.deleteById( cartId );

                return order.getId();
            } else throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "The cart is empty." );
        } else throw shoppingCartNotFoundResponse( cartId );
    }

    private void addOne( final ShoppingCartItemEntity item, final Map<String, OrderPositionEntity> positions ) {
        final String label = item.getLabel();
        final Money price = toMoney( item.getPrice() );

        final OrderPositionEntity defaultPosition = new OrderPositionEntity();
        defaultPosition.setId( UUID.randomUUID() );
        defaultPosition.setItemName( label );
        defaultPosition.setCount( 0 );
        defaultPosition.setSinglePrice( price.toString() );
        defaultPosition.setCombinedPrice( zeroPrice( price ).toString() );

        final OrderPositionEntity position = positions.getOrDefault( label, defaultPosition );
        final Money combinedPrice = toMoney( position.getCombinedPrice() ).plus( price );

        final OrderPositionEntity updatedPosition = new OrderPositionEntity();
        updatedPosition.setId( position.getId() );
        updatedPosition.setItemName( position.getItemName() );
        updatedPosition.setCount( position.getCount()+1 );
        updatedPosition.setSinglePrice( position.getSinglePrice() );
        updatedPosition.setCombinedPrice( combinedPrice.toString() );
        positions.put( label, updatedPosition );
    }

    private Money zeroPrice( final Money price ) {
        return Money.zero( price.getCurrencyUnit() );
    }

    private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "A shopping cart with id "+cartId+" does not exist." );
    }
}
