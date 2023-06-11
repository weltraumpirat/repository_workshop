package com.tobiasgoeschel.workshops.repows.application;

import com.tobiasgoeschel.workshops.repows.RepoWsApplication;
import com.tobiasgoeschel.workshops.repows.adapter.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = { RepoWsApplication.class, ShoppingCartController.class } )
@ComponentScan(basePackages = {"com.tobiasgoeschel.workshops.repows.application", "com.tobiasgoeschel.workshops.repows.persistence"})
class ShoppingCartControllerTest {
    @Autowired
    ShoppingCartController controller;
    @Autowired
    AddShoppingCartItemCommand addCartItem;
    @Autowired
    CheckOutShoppingCartCommand checkOut;
    @Autowired
    CreateEmptyShoppingCartCommand createEmpty;
    @Autowired
    DeleteShoppingCartCommand deleteCart;
    @Autowired
    RemoveShoppingCartItemCommand removeCartItem;
    @Autowired
    ShoppingCartsQuery cartsQuery;
    @Autowired
    ShoppingCartItemsQuery cartItemsQuery;
    @Test
    void shouldWireApplicationContext() {
        assertThat(controller).isNotNull();
        assertThat(addCartItem).isNotNull();
        assertThat(checkOut).isNotNull();
        assertThat(createEmpty).isNotNull();
        assertThat(deleteCart).isNotNull();
        assertThat(removeCartItem).isNotNull();
        assertThat(cartsQuery).isNotNull();
        assertThat(cartItemsQuery).isNotNull();
    }
}
