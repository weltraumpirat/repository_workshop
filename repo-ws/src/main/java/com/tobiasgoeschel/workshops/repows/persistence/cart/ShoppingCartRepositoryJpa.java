package com.tobiasgoeschel.workshops.repows.persistence.cart;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Component
public class ShoppingCartRepositoryJpa implements ShoppingCartRepository {
    private final ShoppingCartCrudRepository repository;

    public ShoppingCartRepositoryJpa( final ShoppingCartCrudRepository repository ) {
        this.repository = repository;
    }

    @Override public List<ShoppingCart> findAll() {
        return StreamSupport.stream( repository.findAll().spliterator(), false )
                               .map( ShoppingCartFactory::restore )
                               .toList();
    }

    @Override public ShoppingCart findById( final UUID id ) {
        final ShoppingCartEntity entity = repository.findById( id )
                                              .orElseThrow(()-> shoppingCartNotFoundResponse( id ));
        return ShoppingCartFactory.restore(entity);
    }

    private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "A shopping cart with id "+cartId+" does not exist." );
    }

    @Override public void add( final ShoppingCart cart ) {
        ShoppingCartEntity entity = ShoppingCartEntity.fromCart( cart );
        repository.save( entity );
    }

    @Override public void addAll( final List<ShoppingCart> carts ) {
       carts.forEach( this::add );
    }

    @Override public void remove( final ShoppingCart cart ) {
        repository.deleteById( cart.getId() );
    }

    @Override public void removeAll( final List<ShoppingCart> carts ) {
        carts.forEach(this::remove);
    }
}
