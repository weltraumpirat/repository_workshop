package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.CheckOutShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.Order;
import com.tobiasgoeschel.workshops.repows.domain.OrderFactory;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartEntity;
import com.tobiasgoeschel.workshops.repows.persistence.order.OrderCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.order.OrderEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
            final ShoppingCartEntity cartEntity = jpaCartsRepo.findById( cartId ).orElseThrow();
            final ShoppingCart cart = ShoppingCartFactory.restore( cartEntity );
            if( cart.getItems().size()>0 ) {
                final Order order = OrderFactory.create(cart );
                final OrderEntity entity = OrderEntity.fromOrder(order);

                jpaOrdersRepo.save( entity );
                jpaCartsRepo.deleteById( cartId );

                return order.getId();
            } else throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "The cart is empty." );
        } else throw shoppingCartNotFoundResponse( cartId );
    }



    private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "A shopping cart with id "+cartId+" does not exist." );
    }
}
