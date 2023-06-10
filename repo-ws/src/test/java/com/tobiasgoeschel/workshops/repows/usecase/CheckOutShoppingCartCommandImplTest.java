package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.CheckOutShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.*;
import com.tobiasgoeschel.workshops.repows.persistence.OrderRepositoryInMemory;
import com.tobiasgoeschel.workshops.repows.persistence.ShoppingCartRepositoryInMemory;
import org.assertj.core.data.TemporalUnitOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static com.tobiasgoeschel.workshops.repows.application.config.MoneyMapper.toMoney;
import static org.assertj.core.api.Assertions.*;

class CheckOutShoppingCartCommandImplTest {
    private static final TemporalUnitOffset DATETIME_PRECISION = within(500, ChronoUnit.MILLIS);
    private ShoppingCartRepository cartRepository;
    private OrderRepository orderRepository;
    private CheckOutShoppingCartCommand command;

    @BeforeEach
    void setUp() {
        cartRepository = new ShoppingCartRepositoryInMemory();
        orderRepository = new OrderRepositoryInMemory();
        final CheckOutService service = new CheckOutService(cartRepository, orderRepository);
        command = new CheckOutShoppingCartCommandImpl(service);
    }

    @Nested
    class GivenTheRepositoryIsEmpty {
        @Nested
        class WhenCheckingOut {
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
    class GivenThereIsAShoppingCart {
        ShoppingCart cart;

        @BeforeEach
        void setUp() {
            cart = ShoppingCartFactory.create();
            cartRepository.add(cart);
        }

        @Nested
        class WhenCheckingOut {
            @Nested
            class AndTheCartMatches {
                @Nested
                class ButTheCartIsEmpty {
                    @Test
                    void shouldThrow400BadRequest() {
                        assertThatExceptionOfType(ResponseStatusException.class)
                                .isThrownBy(() -> command.invoke(cart.getId()))
                                .withMessageMatching("400 BAD_REQUEST \"The cart is empty.\"");
                    }

                    @Test
                    void shouldNotDeleteTheCart() {
                        assertThat(cartRepository.findAll()).hasSize(1);
                    }

                    @Test
                    void shouldNotCreateAnOrder() {
                        assertThat(orderRepository.findAll()).isEmpty();
                    }
                }

                @Nested
                class AndTheCartHasTwoMatchingItems {
                    private List<ShoppingCartItem> items;

                    @BeforeEach
                    public void setUp() {
                        final ShoppingCartItem item = ShoppingCartItemFactory.create("Thing", toMoney("EUR 10"));
                        cart.addItem(item);

                        final ShoppingCartItem otherItem = ShoppingCartItemFactory.create("Thing", toMoney("EUR 10"));
                        cart.addItem(otherItem);
                        items = List.of(item, otherItem);

                        command.invoke(cart.getId());
                    }

                    @Test
                    void shouldDeleteTheCart() {
                        assertThat(cartRepository.findAll()).isEmpty();
                    }

                    @Test
                    void shouldCreateAnOrderWithACurrentTimestampAndOneAccumulatedPositionAndASumTotal() {
                        final List<Order> orders = orderRepository.findAll();
                        assertThat(orders).hasSize(1);
                        final Order order = orders.get(0);
                        assertThat(order.getPositions()).hasSize(1);
                        assertThat(order.getTimestamp().atOffset(ZoneOffset.ofHours(2)))
                                .isCloseToUtcNow(DATETIME_PRECISION);

                        final OrderPosition position = order.getPositions().get(0);
                        assertThat(position.getItemName()).isEqualTo(items.get(0).getLabel());
                        assertThat(position.getItemName()).isEqualTo(items.get(1).getLabel());
                        assertThat(position.getCombinedPrice()).isEqualTo(toMoney("EUR 20.00"));
                        assertThat(position.getSinglePrice()).isEqualTo(toMoney("EUR 10.00"));

                        assertThat(order.getTotal()).isEqualTo(toMoney("EUR 20.00"));
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
}
