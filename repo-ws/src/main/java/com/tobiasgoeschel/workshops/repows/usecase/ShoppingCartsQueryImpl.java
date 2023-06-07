package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.adapter.ShoppingCartsQuery;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;

import java.util.List;
import java.util.stream.StreamSupport;

public class ShoppingCartsQueryImpl implements ShoppingCartsQuery {
    private final ShoppingCartCrudRepository jpaCartsRepo;

    public ShoppingCartsQueryImpl( ShoppingCartCrudRepository jpaCartsRepo ) {
        this.jpaCartsRepo = jpaCartsRepo;
    }

    @Override public List<ShoppingCart> invoke() {
        return StreamSupport.stream( jpaCartsRepo.findAll().spliterator(), false )
                   .map( ShoppingCartFactory::restore )
                   .toList();
    }
}
