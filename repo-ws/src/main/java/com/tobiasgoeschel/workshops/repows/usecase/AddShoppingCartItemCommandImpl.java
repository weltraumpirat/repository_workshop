package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.AddShoppingCartItemCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItem;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class AddShoppingCartItemCommandImpl implements AddShoppingCartItemCommand {
    private final ShoppingCartCrudRepository jpaCartsRepo;

    public AddShoppingCartItemCommandImpl( final ShoppingCartCrudRepository jpaCartsRepo ) {
        this.jpaCartsRepo = jpaCartsRepo;
    }


    @Override public void invoke( final UUID cartId, final ShoppingCartItem item ) {
        final ShoppingCartEntity cartEntity = jpaCartsRepo
                                            .findById( cartId )
                                            .orElseThrow( () -> shoppingCartNotFoundResponse( cartId ) );
        final ShoppingCart cart = ShoppingCartFactory.restore( cartEntity );
        cart.addItem( item );


        jpaCartsRepo.save( ShoppingCartEntity.fromCart( cart )  );
    }

    private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "A shopping cart with id "+cartId+" does not exist." );
    }
}
