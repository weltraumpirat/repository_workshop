package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.ShoppingCartItemsQuery;
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

class ShoppingCartItemsQueryImplTest {
    private ShoppingCartRepository repository;

    private ShoppingCartItemsQuery query;

    @BeforeEach
    void setUp() {
        repository = new ShoppingCartRepositoryInMemory();
        query = new ShoppingCartItemsQueryImpl(repository);
    }

    @Nested
    class GivenTheRepositoryIsEmpty {
        @Test
        void shouldThrow404CartNotFound() {
            assertThatExceptionOfType(ResponseStatusException.class)
                    .isThrownBy(() -> query.invoke(UUID.randomUUID()))
                    .withMessageMatching(
                            "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"");
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
        class WhenQueryingCartItems {
            @Nested
            class AndTheCartMatches {
                @Nested
                class AndTheCartIsEmpty {
                    @Test
                    void shouldReturnAnEmptyList() {
                        assertThat(query.invoke(cart.getId())).isEmpty();
                    }
                }

                @Nested
                class AndTheCartHasAnItem {
                    ShoppingCartItem item;

                    @BeforeEach
                    void setUp() {
                        item = ShoppingCartItemFactory.create("Thing", toMoney("EUR 10"));
                        cart.addItem(item);

                    }

                    @Test
                    void shouldReturnListWithItem() {
                        final List<ShoppingCartItem> items = query.invoke(cart.getId());
                        assertThat(items).hasSize(1);
                        assertThat(items).containsExactly(item);
                    }
                }
            }

            @Nested
            class AndTheCartDoesNotMatch {
                @Test
                void shouldThrow404CartNotFound() {
                    assertThatExceptionOfType(ResponseStatusException.class)
                            .isThrownBy(() -> query.invoke(UUID.randomUUID()))
                            .withMessageMatching(
                                    "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"");
                }
            }
        }
    }

}
