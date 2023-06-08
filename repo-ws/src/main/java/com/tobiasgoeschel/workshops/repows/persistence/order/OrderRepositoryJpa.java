package com.tobiasgoeschel.workshops.repows.persistence.order;

import com.tobiasgoeschel.workshops.repows.domain.Order;
import com.tobiasgoeschel.workshops.repows.domain.OrderFactory;
import com.tobiasgoeschel.workshops.repows.domain.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class OrderRepositoryJpa implements OrderRepository {
    private final OrderCrudRepository repository;

    public OrderRepositoryJpa( final OrderCrudRepository repository ) {
        this.repository = repository;
    }

    @Override public List<Order> findAll() {
        return StreamSupport.stream( repository.findAll().spliterator(), false)
                   .map( OrderFactory::restore ).collect( Collectors.toList() );
    }

    @Override public void add( final Order order ) {
       repository.save( OrderEntity.fromOrder( order ) );
    }

    @Override public void addAll( final List<Order> orders ) {
        orders.forEach( this::add );
    }

    @Override public void remove( final Order order ) {
       repository.deleteById( order.getId() );
    }

    @Override public void removeAll( final List<Order> orders ) {
       orders.forEach( this::remove );
    }
}
