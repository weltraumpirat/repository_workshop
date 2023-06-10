package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.CreateEmptyShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;
import com.tobiasgoeschel.workshops.repows.persistence.ShoppingCartRepositoryInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateEmptyShoppingCartCommandImplTest {
    ShoppingCartRepository repository;
    CreateEmptyShoppingCartCommand command;

    @BeforeEach
    void setUp() {
        repository = new ShoppingCartRepositoryInMemory();
        command = new CreateEmptyShoppingCartCommandImpl(repository);
    }

    @Nested
    class GivenTheRepositoryIsEmpty {
        @Nested
        class WhenCreatingAnEmptyShoppingCart {
            ShoppingCart cart;

            @BeforeEach
            void setUp() {
                cart = command.invoke();
            }

            @Test
            void shouldReturnTheNewlyCreatedShoppingCart() {
                assertThat(cart).isNotNull();
            }

            @Test
            void shouldStoreTheCart() {
                final List<ShoppingCart> carts = repository.findAll();
                assertThat(carts).hasSize(1);
                assertThat(carts.get(0)).isEqualTo(cart);
            }

        }
    }

}
