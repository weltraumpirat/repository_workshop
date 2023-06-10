package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.domain.*;
import com.tobiasgoeschel.workshops.repows.persistence.ShoppingCartRepositoryInMemory;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static com.tobiasgoeschel.workshops.repows.application.config.MoneyMapper.toMoney;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AddShoppingCartItemCommandImplTest {
    private ShoppingCartRepository repository;

    private AddShoppingCartItemCommandImpl command;

    @BeforeEach
    void setUp() {
        repository = new ShoppingCartRepositoryInMemory();
        command = new AddShoppingCartItemCommandImpl(repository);
    }

    @Nested
    class GivenTheRepositoryIsEmpty {


        @Nested
        class WhenAddingAnItem {
            @Test
            void shouldThrow404CartNotFound() {
                assertThatExceptionOfType(ResponseStatusException.class)
                        .isThrownBy(() -> command.invoke(
                                UUID.randomUUID(),
                                ShoppingCartItemFactory.create("toto", Money.zero(CurrencyUnit.EUR))))
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
        class WhenAddingAnItem {
            ShoppingCartItem item;

            @BeforeEach
            void setUp() {
                item = ShoppingCartItemFactory.create("Thing", toMoney("EUR 10"));
            }

            @Nested
            class AndTheCartMatches {

                @BeforeEach
                void setUp() {
                    command.invoke(cart.getId(), item);
                }

                @Test
                void shouldAddTheItem() {
                    final List<ShoppingCartItem> items = repository.findById(cart.getId()).getItems();
                    assertThat(items).hasSize(1);
                    assertThat(items.get(0)).isEqualTo(item);
                }
            }

            @Nested
            class AndTheCartDoesNotMatch {
                @Test
                void shouldThrow404CartNotFound() {
                    assertThatExceptionOfType(ResponseStatusException.class)
                            .isThrownBy(() -> command.invoke(UUID.randomUUID(), item))
                            .withMessageMatching(
                                    "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"");
                }
            }
        }
    }
}
