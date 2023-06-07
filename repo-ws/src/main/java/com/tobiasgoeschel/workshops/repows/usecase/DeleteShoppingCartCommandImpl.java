package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.DeleteShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class DeleteShoppingCartCommandImpl implements DeleteShoppingCartCommand {
    private final ShoppingCartCrudRepository jpaCartsRepo;

    public DeleteShoppingCartCommandImpl( final ShoppingCartCrudRepository jpaCartsRepo ) {
        this.jpaCartsRepo = jpaCartsRepo;
    }

    @Override
    public void invoke( UUID cartId) {
        if( jpaCartsRepo.existsById( cartId ) ) {
            jpaCartsRepo.deleteById( cartId );
        } else throw shoppingCartNotFoundResponse( cartId );
    }

    private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "A shopping cart with id "+cartId+" does not exist." );
    }
}
