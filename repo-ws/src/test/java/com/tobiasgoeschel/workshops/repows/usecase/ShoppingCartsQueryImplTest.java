package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.ShoppingCartsQuery;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartFactory;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartRepository;
import com.tobiasgoeschel.workshops.repows.persistence.ShoppingCartRepositoryInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShoppingCartsQueryImplTest {
    private ShoppingCartRepository repository;

    private ShoppingCartsQuery query;

    @BeforeEach
    void setUp() {
        repository = new ShoppingCartRepositoryInMemory();
        query = new ShoppingCartsQueryImpl(repository);
    }

    @Nested
    class GivenTheRepositoryIsEmpty {
        @Test
        void shouldReturnAnEmptyList() {
            assertThat(query.invoke()).isEmpty();
        }
    }

    @Nested
    class GivenAShoppingCartExists {
        ShoppingCart cart;

        @BeforeEach
        void setUp() {
            cart = ShoppingCartFactory.create();
            repository.add(cart);
        }

        @Test
        void shouldReturnListWithCart() {
            final List<ShoppingCart> carts = query.invoke();
            assertThat(carts).hasSize(1);
            assertThat(carts).containsExactly(cart);
        }
    }

}
