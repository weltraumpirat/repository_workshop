package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.RemoveShoppingCartItemCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class RemoveShoppingCartItemCommandImpl implements RemoveShoppingCartItemCommand {

    private final ShoppingCartCrudRepository jpaCartRepository;

    public RemoveShoppingCartItemCommandImpl( final ShoppingCartCrudRepository jpaCartRepository ) {
        this.jpaCartRepository = jpaCartRepository;
    }

    @Override public void invoke( final UUID cartId, final UUID itemId ) {
        if( jpaCartRepository.existsById( cartId )) {
            final ShoppingCartEntity entity = jpaCartRepository.findById( cartId ).orElseThrow();
            final ShoppingCart cart = ShoppingCartFactory.restore( entity );
            cart.removeItem( itemId );
            jpaCartRepository.save( ShoppingCartEntity.fromCart( cart ) );
        } else throw shoppingCartNotFoundResponse( cartId );
    }

    private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "A shopping cart with id "+cartId+" does not exist." );
    }
}
