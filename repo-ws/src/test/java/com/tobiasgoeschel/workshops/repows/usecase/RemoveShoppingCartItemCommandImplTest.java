package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.RemoveShoppingCartItemCommand;
import com.tobiasgoeschel.workshops.repows.domain.*;
import com.tobiasgoeschel.workshops.repows.persistence.ShoppingCartRepositoryInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static com.tobiasgoeschel.workshops.repows.application.config.MoneyMapper.toMoney;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RemoveShoppingCartItemCommandImplTest {
    private ShoppingCartRepository repository;

    private RemoveShoppingCartItemCommand command;

    @BeforeEach
    void setUp() {
        repository = new ShoppingCartRepositoryInMemory();
        command = new RemoveShoppingCartItemCommandImpl(repository);
    }

    @Nested
    class GivenTheRepositoryIsEmpty {
        @Nested
        class WhenRemovingAnItem {
            @Test
            void shouldThrow404CartNotFound() {
                assertThatExceptionOfType(ResponseStatusException.class)
                        .isThrownBy(() -> command.invoke(UUID.randomUUID(),UUID.randomUUID()))
                        .withMessageMatching(
                                "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"");
            }
        }
    }

    @Nested
    class GivenThereIsAShoppingCart {
        ShoppingCart cart;

        @BeforeEach
        void setUp() {
            cart = ShoppingCartFactory.create();
            repository.add(cart);
        }

        @Nested
        class WhenRemovingAnItem {
            ShoppingCartItem item;

            @BeforeEach
            void setUp() {
                item = ShoppingCartItemFactory.create("Thing", toMoney("EUR 10"));
                cart.addItem(item);
            }

            @Nested
            class AndTheCartMatches {

                @BeforeEach
                void setUp() {
                    command.invoke(cart.getId(), item.getId());
                }

                @Test
                void shouldRemoveTheItem() {
                    final List<ShoppingCartItem> items = repository.findById(cart.getId()).getItems();
                    assertThat(items).isEmpty();
                }
            }

            @Nested
            class AndTheCartDoesNotMatch {
                @Test
                void shouldThrow404CartNotFound() {
                    assertThatExceptionOfType(ResponseStatusException.class)
                            .isThrownBy(() -> command.invoke(UUID.randomUUID(), item.getId()))
                            .withMessageMatching(
                                    "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"");
                }
            }
        }
    }
}
