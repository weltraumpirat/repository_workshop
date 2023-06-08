package com.tobiasgoeschel.workshops.repows.application.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.tobiasgoeschel.workshops.repows.usecase.*;
import com.tobiasgoeschel.workshops.repows.adapter.*;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.persistence.order.OrderCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RepoWSConfig {
    @Bean ShoppingCartsQuery createShoppingCartsQuery(
        @Autowired final ShoppingCartCrudRepository crudRepository ) {
        return new ShoppingCartsQueryImpl( crudRepository );
    }

    @Bean ShoppingCartItemsQuery createShoppingCartItemsQuery(
        @Autowired final ShoppingCartCrudRepository crudRepository ) {
        return new ShoppingCartItemsQueryImpl( crudRepository );
    }

    @Bean CreateEmptyShoppingCartCommand createEmptyShoppingCartCommand(
        @Autowired final ShoppingCartCrudRepository crudRepository ) {
        return new CreateEmptyShoppingCartCommandImpl( crudRepository );
    }

    @Bean DeleteShoppingCartCommand deleteShoppingCartCommand(
        @Autowired final ShoppingCartCrudRepository crudRepository ) {
        return new DeleteShoppingCartCommandImpl( crudRepository );
    }

    @Bean AddShoppingCartItemCommand addShoppingCartItemCommand(
        @Autowired final ShoppingCartCrudRepository crudRepository ) {
        return new AddShoppingCartItemCommandImpl( crudRepository );
    }

    @Bean RemoveShoppingCartItemCommand removeShoppingCartItemCommand(
        @Autowired final ShoppingCartCrudRepository crudRepository ) {
        return new RemoveShoppingCartItemCommandImpl( crudRepository );
    }

    @Bean CheckOutShoppingCartCommand checkOutShoppingCartCommand(
        @Autowired final ShoppingCartCrudRepository crudRepository,
        @Autowired final OrderCrudRepository orderCrudRepository ) {
        return new CheckOutShoppingCartCommandImpl( crudRepository, orderCrudRepository );
    }

    @Bean
    @Primary
    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectmapper = new ObjectMapper();
        objectmapper.setVisibility( FIELD, ANY );
        objectmapper.registerModule( new ParameterNamesModule() );
        objectmapper.registerModule( new MoneyModule() );
        return objectmapper;
    }

}
