package com.tobiasgoeschel.workshops.repows.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class CheckOutService {
    private final ShoppingCartRepository cartRepository;
    private final OrderRepository        orderRepository;

    public CheckOutService( final ShoppingCartRepository cartRepository, final OrderRepository orderRepository ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    public Order checkOut( final UUID cartId ) {
        final ShoppingCart cart = cartRepository.findById( cartId );
        if( cart.getItems().size()>0 ) {
            final Order order = OrderFactory.create( cart );
            orderRepository.add( order );
            cartRepository.remove( cart );
            return order;
        } else throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "The cart is empty." );
    }
}
