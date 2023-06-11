package com.tobiasgoeschel.workshops.repows.application.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.tobiasgoeschel.workshops.repows.adapter.*;
import com.tobiasgoeschel.workshops.repows.domain.CheckOutService;
import com.tobiasgoeschel.workshops.repows.persistence.cart.ShoppingCartRepositoryJpa;
import com.tobiasgoeschel.workshops.repows.persistence.order.OrderRepositoryJpa;
import com.tobiasgoeschel.workshops.repows.usecase.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RepoWSConfig {
    @Bean ShoppingCartsQuery createShoppingCartsQuery(
        @Autowired final ShoppingCartRepositoryJpa repositoryJpa ) {
        return new ShoppingCartsQueryImpl( repositoryJpa );
    }

    @Bean ShoppingCartItemsQuery createShoppingCartItemsQuery(
        @Autowired final ShoppingCartRepositoryJpa repositoryJpa ) {
        return new ShoppingCartItemsQueryImpl( repositoryJpa );
    }

    @Bean CreateEmptyShoppingCartCommand createEmptyShoppingCartCommand(
        @Autowired final ShoppingCartRepositoryJpa repositoryJpa ) {
        return new CreateEmptyShoppingCartCommandImpl( repositoryJpa );
    }

    @Bean DeleteShoppingCartCommand deleteShoppingCartCommand(
        @Autowired final ShoppingCartRepositoryJpa repositoryJpa ) {
        return new DeleteShoppingCartCommandImpl( repositoryJpa );
    }

    @Bean AddShoppingCartItemCommand addShoppingCartItemCommand(
        @Autowired final ShoppingCartRepositoryJpa repositoryJpa ) {
        return new AddShoppingCartItemCommandImpl( repositoryJpa );
    }

    @Bean RemoveShoppingCartItemCommand removeShoppingCartItemCommand(
        @Autowired final ShoppingCartRepositoryJpa repositoryJpa ) {
        return new RemoveShoppingCartItemCommandImpl( repositoryJpa );
    }

    @Bean CheckOutShoppingCartCommand checkOutShoppingCartCommand(
        @Autowired final ShoppingCartRepositoryJpa repositoryJpa,
        @Autowired final OrderRepositoryJpa orderRepositoryJpa ) {
        final CheckOutService service = new CheckOutService( repositoryJpa, orderRepositoryJpa );
        return new CheckOutShoppingCartCommandImpl( service );
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
