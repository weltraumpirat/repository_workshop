package com.tobiasgoeschel.workshops.repows.application.persistence.cart;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Component
public class ShoppingCartRepositoryJpa implements ShoppingCartRepository {
    private final List<ShoppingCart>         carts = new ArrayList<>();
    private final ShoppingCartCrudRepository repository;

    public ShoppingCartRepositoryJpa( final ShoppingCartCrudRepository repository ) {
        this.repository = repository;
    }

    @Override public List<ShoppingCart> findAll() {
        final List<ShoppingCart> carts = StreamSupport.stream( repository.findAll().spliterator(), false )
                                             .map( ShoppingCartFactory::restore )
                                             .toList();
        if( this.carts.size() == 0 ) {
            this.carts.addAll( carts );
        } else {
            final List<ShoppingCart> newCarts =
                carts.stream().filter( c -> this.carts.stream().filter( i -> i.getId().equals( c.getId() ) ).findAny()
                                                .isEmpty() ).toList();
            this.carts.addAll( newCarts );
        }
        return this.carts;
    }

    @Override public ShoppingCart findById( final UUID id ) {
        final Optional<ShoppingCart> localCart = this.carts.stream()
                                                .filter( c -> c.getId().equals( id ) )
                                                .findAny();
        if( localCart.isPresent() )
            return localCart.get();
        else {
            final ShoppingCart cart =
                repository.findById( id )
                    .map( ShoppingCartFactory::restore)
                    .orElseThrow( () -> shoppingCartNotFoundResponse( id ) );
            this.carts.add( cart );
            return cart;
        }
    }

    private ResponseStatusException shoppingCartNotFoundResponse( final UUID cartId ) {
        return new ResponseStatusException( HttpStatus.NOT_FOUND,
                                            "A shopping cart with id "+cartId+" does not exist." );
    }

    @Override public void add( final ShoppingCart cart ) {
        carts.add( cart );
        ShoppingCartEntity entity = ShoppingCartEntity.fromCart( cart );
        repository.save( entity );
    }

    @Override public void addAll( final List<ShoppingCart> carts ) {
        carts.forEach( this::add );
    }

    @Override public void remove( final ShoppingCart cart ) {
        carts.remove( cart );
        repository.deleteById( cart.getId() );
    }

    @Override public void removeAll( final List<ShoppingCart> carts ) {
        carts.forEach( this::remove );
    }
    @Override public void removeAll(  ) {
        List<ShoppingCart> cartsToSave = List.copyOf(  carts );
        cartsToSave.forEach( this::remove );
    }

    public void flush() {
        final List<ShoppingCart> savedCarts = StreamSupport.stream( repository.findAll().spliterator(), false )
                                             .map( ShoppingCartFactory::restore )
                                             .toList();
        carts.stream().filter(c-> notSaved(c,savedCarts) || changed(c, savedCarts)).forEach(c -> repository.save( ShoppingCartEntity.fromCart( c ) ));
    }


    private boolean changed( final ShoppingCart cart, final List<ShoppingCart> savedCarts ) {
        return savedCarts.stream().anyMatch( c -> c.getId().equals( cart.getId() ) && c.getItems().size() != cart.getItems().size() );
    }

    private boolean notSaved( final ShoppingCart cart, final List<ShoppingCart> savedCarts ) {
        return savedCarts.stream().filter( c -> c.getId().equals( cart.getId() ) ).findAny().isEmpty();
    }

}
