package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;
import com.tobiasgoeschel.workshops.repows.persistence.ShoppingCartRepositoryInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DeleteShoppingCartCommandImplTest {
    private ShoppingCartRepository repository;

    private DeleteShoppingCartCommandImpl command;

    @BeforeEach
    void setUp() {
        repository = new ShoppingCartRepositoryInMemory();
        command = new DeleteShoppingCartCommandImpl(repository);
    }

    @Nested
    class GivenTheRepositoryIsEmpty {
        @Nested
        class WhenDeletingACart {
            @Test
            void shouldThrow404CartNotFound() {
                assertThatExceptionOfType(ResponseStatusException.class)
                        .isThrownBy(() -> command.invoke(UUID.randomUUID()))
                        .withMessageMatching(
                                "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"");
            }
        }
    }

    @Nested
    class GivenTheAShoppingCartExists {
        ShoppingCart cart;

        @BeforeEach
        void setUp() {
            cart = ShoppingCartFactory.create();
            repository.add(cart);
        }

        @Nested
        class AndTheCartMatches {
            @Nested
            class WhenDeletingACart {
                @BeforeEach
                void setUp() {
                    command.invoke(cart.getId());
                }

                @Test
                void shouldDeleteTheCart() {
                    assertThat(repository.findAll()).isEmpty();
                }
            }
        }

        @Nested
        class AndTheCartDoesNotMatch {
            @Test
            void shouldThrow404CartNotFound() {
                assertThatExceptionOfType(ResponseStatusException.class)
                        .isThrownBy(() -> command.invoke(UUID.randomUUID()))
                        .withMessageMatching(
                                "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"");
            }
        }
    }

}
