package com.tobiasgoeschel.workshops.repows.persistence.order;

import com.tobiasgoeschel.workshops.repows.domain.Order;
import com.tobiasgoeschel.workshops.repows.domain.OrderFactory;
import com.tobiasgoeschel.workshops.repows.domain.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class OrderRepositoryJpa implements OrderRepository {
    private final List<Order>         orders = new ArrayList<>();
    private final OrderCrudRepository repository;

    public OrderRepositoryJpa( final OrderCrudRepository repository ) {
        this.repository = repository;
    }

    @Override public List<Order> findAll() {
        final List<Order> savedOrders = StreamSupport.stream( repository.findAll().spliterator(), false )
                                            .map( OrderFactory::restore ).toList();
        if( this.orders.size() == 0 ) {
            this.orders.addAll( savedOrders );
        } else {
            final List<Order> newOrders =
                savedOrders.stream()
                    .filter( c -> this.orders.stream().filter( i -> i.getId().equals( c.getId() ) ).findAny()
                                      .isEmpty() ).toList();
            this.orders.addAll( newOrders );
        }
        return this.orders;
    }

    @Override public void add( final Order order ) {
        orders.add( order );
        repository.save( OrderEntity.fromOrder( order ) );
    }

    @Override public void addAll( final List<Order> orders ) {
        orders.forEach( this::add );
    }

    @Override public void remove( final Order order ) {
        orders.remove(order);
        repository.deleteById( order.getId() );
    }

    @Override public void removeAll( final List<Order> orders ) {
        orders.forEach( this::remove );
    }

    @Override public void removeAll() {
        List<Order> ordersToSave = List.copyOf( orders );
        ordersToSave.forEach( this::remove );
    }

    public void flush() {
        final List<Order> savedOrders = StreamSupport.stream( repository.findAll().spliterator(), false )
                                            .map( OrderFactory::restore )
                                            .toList();
        orders.stream().filter( o -> notSaved( o, savedOrders ) || changed( o, savedOrders ) )
            .forEach( o -> repository.save( OrderEntity.fromOrder( o ) ) );
    }


    private boolean changed( final Order order, final List<Order> savedOrders ) {
        return savedOrders.stream().anyMatch(
            o -> o.getId().equals( order.getId() ) && o.getPositions().size() != order.getPositions().size() );
    }

    private boolean notSaved( final Order order, final List<Order> savedOrders ) {
        return savedOrders.stream().filter( o -> o.getId().equals( order.getId() ) ).findAny().isEmpty();
    }

}
