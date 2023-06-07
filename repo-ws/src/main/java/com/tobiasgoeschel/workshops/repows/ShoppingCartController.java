package com.tobiasgoeschel.workshops.repows;

import static com.tobiasgoeschel.workshops.repows.MoneyMapper.toMoney;
import com.tobiasgoeschel.workshops.repows.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.cart.ShoppingCartEntity;
import com.tobiasgoeschel.workshops.repows.cart.ShoppingCartItemEntity;
import com.tobiasgoeschel.workshops.repows.order.OrderCrudRepository;
import com.tobiasgoeschel.workshops.repows.order.OrderEntity;
import com.tobiasgoeschel.workshops.repows.order.OrderPositionEntity;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.StreamSupport;

@CrossOrigin( origins = { "http://localhost:3000", "http://localhost:3001", "http://localhost" } )

@RestController
public class ShoppingCartController {
    @Autowired
    private OrderCrudRepository        jpaOrdersRepo;
    @Autowired
    private ShoppingCartCrudRepository jpaCartsRepo;


    @GetMapping( "/api/cart" )
    public List<ShoppingCartEntity> getCarts() {
        return StreamSupport.stream( jpaCartsRepo.findAll().spliterator(), false )
                   .toList();
    }

    @GetMapping( "/api/cart/{cartId}" )
    public List<ShoppingCartItemEntity> getCartItems( @PathVariable final UUID cartId ) {
        if( jpaCartsRepo.existsById( cartId ) ) {
            return jpaCartsRepo.findById( cartId )
                       .map( ShoppingCartEntity::getItems )
                       .orElse( Collections.emptyList() );
        } else throw shoppingCartNotFoundResponse( cartId );
    }

    @PostMapping( "/api/cart" )
    public ShoppingCartEntity createEmptyCart() {
        final ShoppingCartEntity cart = new ShoppingCartEntity( UUID.randomUUID(), new ArrayList<>() );
        jpaCartsRepo.save( cart );
        return cart;
    }

    @DeleteMapping( "/api/cart/{cartId}" )
    @Transactional
    public void deleteCart( @PathVariable final UUID cartId ) {
        if( jpaCartsRepo.existsById( cartId ) ) {
            jpaCartsRepo.deleteById( cartId );
        } else throw shoppingCartNotFoundResponse( cartId );
    }

    @PostMapping( "/api/cart/{cartId}" )
    public void addItem( @PathVariable final UUID cartId, @RequestBody final ShoppingCartItemEntity item ) {
        if( item.getId() == null ) item.setId( UUID.randomUUID() );

        final ShoppingCartEntity cart = jpaCartsRepo
                                            .findById( cartId )
                                            .orElseThrow( () -> shoppingCartNotFoundResponse( cartId ) );
        cart.getItems().add( item );
        jpaCartsRepo.save( cart );
    }

    @DeleteMapping( "/api/cart/{cartId}/{itemId}" )
    public void removeItem( @PathVariable final UUID cartId, @PathVariable UUID itemId ) {
        if( jpaCartsRepo.existsById( cartId )) {
            final ShoppingCartEntity cart = jpaCartsRepo.findById( cartId ).orElseThrow();
            final List<ShoppingCartItemEntity> items = cart.getItems();
            items.stream()
                .filter( i -> i.getId().equals( itemId ) )
                .findFirst()
                .or( ()-> {
                    throw shoppingCartItemNotFoundResponse( cartId, itemId );
                })
                .ifPresent( (i)->{
                    cart.getItems().remove( i );
                    jpaCartsRepo.save( cart );
                } );
        } else throw shoppingCartNotFoundResponse( cartId );
    }

    @PostMapping( "/api/cart/{cartId}/checkout" )
    @Transactional
    public UUID checkOutShoppingCart( @PathVariable final UUID cartId ) {
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
            }
            else throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "The cart is empty." );
        } else throw shoppingCartNotFoundResponse( cartId );
    }

    private void addOne( final ShoppingCartItemEntity item, final Map<String, OrderPositionEntity> positions ) {
        final String label = item.getLabel();
        final Money price = toMoney(item.getPrice());

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
    private ResponseStatusException shoppingCartItemNotFoundResponse( final UUID cartId, final UUID cartItemId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "An item with id "+cartItemId+" does not exist in shopping cart with id "+cartId+"." );
    }
}
