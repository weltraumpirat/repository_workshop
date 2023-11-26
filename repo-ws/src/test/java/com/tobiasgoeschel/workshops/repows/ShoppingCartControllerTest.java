package com.tobiasgoeschel.workshops.repows;

import com.tobiasgoeschel.workshops.repows.cart.ShoppingCartCrudRepository;
import com.tobiasgoeschel.workshops.repows.cart.ShoppingCartEntity;
import com.tobiasgoeschel.workshops.repows.cart.ShoppingCartItemEntity;
import com.tobiasgoeschel.workshops.repows.order.OrderCrudRepository;
import com.tobiasgoeschel.workshops.repows.order.OrderEntity;
import com.tobiasgoeschel.workshops.repows.order.OrderPositionEntity;
import org.assertj.core.data.TemporalUnitOffset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest()
class ShoppingCartControllerTest {
    private static final TemporalUnitOffset DATETIME_PRECISION = within( 500, ChronoUnit.MILLIS );

    @Autowired
    private ShoppingCartController     controller;
    @Autowired
    private ShoppingCartCrudRepository cartRepository;
    @Autowired
    private OrderCrudRepository        orderRepository;

    @Nested
    class GivenTheRepositoryIsEmpty {
        @Nested
        class WhenQueryingAllCarts {
            @Test
            void shouldReturnEmptyList() {
                assertThat( controller.getCarts() )
                    .isEqualTo( emptyList() );
            }
        }

        @Nested
        class WhenQueryingACartItemsById {
            @Test
            void shouldThrow404NotFound() {
                assertThatExceptionOfType( ResponseStatusException.class )
                    .isThrownBy( () -> controller.getCartItems( UUID.randomUUID() ) )
                    .withMessageMatching(
                        "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"" );
            }
        }

        @Nested
        class WhenDeletingACart {
            @Test
            void shouldThrow404NotFound() {
                assertThatExceptionOfType( ResponseStatusException.class )
                    .isThrownBy( () -> controller.deleteCart( UUID.randomUUID() ) )
                    .withMessageMatching(
                        "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"" );
            }
        }
    }

    @Nested
    class GivenThereIsOneShoppingCart {
        private ShoppingCartEntity shoppingCart;

        @BeforeEach
        public void setUp() {
            shoppingCart = controller.createEmptyCart();
        }

        @Nested
        class WhenQueryingAllCarts {
            @Test
            void shouldReturnOneShoppingCart() {
                assertThat( controller.getCarts() ).isEqualTo( List.of( shoppingCart ) );
            }
        }

        @Nested
        class WhenQueryingCartItemsById {
            @Nested
            class AndTheCartMatches {
                @Nested
                class AndTheCartHasNoItems {
                    @Test
                    void shouldReturnEmptyList() {
                        assertThat( controller.getCartItems( shoppingCart.getId() ) ).hasSize( 0 );
                    }
                }

                @Nested
                class AndTheCartHasAnItem {

                    private ShoppingCartItemEntity item;

                    @BeforeEach
                    public void setUp() {
                        item = new ShoppingCartItemEntity();
                        item.setId( UUID.randomUUID() );
                        item.setLabel( "Thing" );
                        item.setPrice( "10 €" );
                        controller.addItem( shoppingCart.getId(), item );
                    }

                    @Test
                    void shouldReturnRequestedShoppingCartItems() {
                        final List<ShoppingCartItemEntity> cartItems = controller.getCartItems( shoppingCart.getId() );
                        assertThat( cartItems.size() ).isEqualTo( 1 );
                        assertThat( cartItems.get( 0 ) )
                            .isEqualTo( item );
                    }
                }
            }

            @Nested
            class AndTheCartDoesNoMatch {
                @Test
                void shouldThrow404NotFound() {
                    assertThatExceptionOfType( ResponseStatusException.class )
                        .isThrownBy( () -> controller.getCartItems( UUID.randomUUID() ) )
                        .withMessageMatching(
                            "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"" );
                }
            }
        }

        @Nested
        class WhenRemovingAnItem {
            @Nested
            class AndTheCartMatches {
                private UUID cartId;

                @BeforeEach
                public void setUp() {
                    cartId = shoppingCart.getId();
                }

                @Nested
                class AndTheCartHasAnItem {

                    private ShoppingCartItemEntity item;

                    @BeforeEach
                    public void setUp() {
                        item = new ShoppingCartItemEntity();
                        item.setId( UUID.randomUUID() );
                        item.setLabel( "Thing" );
                        item.setPrice( "10 €" );
                        controller.addItem( cartId, item );
                    }

                    @Nested
                    class AndTheItemMatches {
                        @BeforeEach
                        public void setUp() {
                            controller.removeItem( cartId, item.getId() );
                        }

                        @Test
                        void shouldRemoveTheItem() {
                            assertThat( controller.getCartItems( cartId ) ).isEmpty();
                        }
                    }

                    @Nested
                    class AndTheItemDoesNoMatch {
                        @Test
                        void shouldThrow404NotFound() {
                            assertThatExceptionOfType( ResponseStatusException.class )
                                .isThrownBy( () -> controller.removeItem( cartId, UUID.randomUUID() ) )
                                .withMessageMatching(
                                    "404 NOT_FOUND \"An item with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist in shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}.\"" );
                        }
                    }
                }

                @Nested
                class AndTheCartHasNoItems {
                    @Test
                    void shouldThrow404NotFound() {
                        assertThatExceptionOfType( ResponseStatusException.class )
                            .isThrownBy( () -> controller.removeItem( cartId, UUID.randomUUID() ) )
                            .withMessageMatching(
                                "404 NOT_FOUND \"An item with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist in shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}.\"" );
                    }
                }
            }

            @Nested
            class AndTheCartDoesNoMatch {
                @Test
                void shouldThrow404NotFound() {
                    assertThatExceptionOfType( ResponseStatusException.class )
                        .isThrownBy( () -> controller.removeItem( UUID.randomUUID(), UUID.randomUUID() ) )
                        .withMessageMatching(
                            "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"" );
                }
            }
        }


        @Nested
        class WhenDeletingACart {
            @Nested
            class AndTheCartMatches {
                @BeforeEach
                public void setUp() {
                    controller.deleteCart( shoppingCart.getId() );
                }

                @Test
                void shouldDeleteTheCart() {
                    assertThat( cartRepository.findAll() ).isEmpty();
                }
            }

            @Nested
            class AndTheCartDoesNoMatch {
                @Test
                void shouldThrow404NotFound() {
                    assertThatExceptionOfType( ResponseStatusException.class )
                        .isThrownBy( () -> controller.deleteCart( UUID.randomUUID() ) )
                        .withMessageMatching(
                            "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"" );
                }
            }
        }

        @Nested
        class WhenCheckingOutTheCart {
            @Nested
            class AndTheCartMatches {
                private UUID cartId;

                @BeforeEach
                void setUp() {
                   cartId = shoppingCart.getId();
                }

                @Nested
                class AndTheCartIsEmpty {
                    @BeforeEach
                    void setUp() {
                        try {
                            controller.checkOutShoppingCart( cartId );
                        } catch( Exception ignore ) {
                        }
                    }

                    @Test
                    void shouldThrow400BadRequest() {
                        assertThatExceptionOfType( ResponseStatusException.class )
                            .isThrownBy( () -> controller.checkOutShoppingCart( cartId ) )
                            .withMessageMatching( "400 BAD_REQUEST \"The cart is empty.\"" );
                    }

                    @Test
                    void shouldNotDeleteTheCart() {
                        assertThat( cartRepository.findAll() ).hasSize( 1 );
                    }

                    @Test
                    void shouldNotCreateAnOrder() {
                        assertThat( orderRepository.findAll() ).isEmpty();
                    }
                }

                @Nested
                class AndTheCartHasTwoMatchingItems {
                    private List<ShoppingCartItemEntity> items;
                    @BeforeEach
                    public void setUp() {
                        ShoppingCartItemEntity item = new ShoppingCartItemEntity();
                        item.setId( UUID.randomUUID() );
                        item.setLabel( "Thing" );
                        item.setPrice( "EUR 10" );
                        controller.addItem( cartId, item );
                        ShoppingCartItemEntity otherItem = new ShoppingCartItemEntity();
                        otherItem.setId( UUID.randomUUID() );
                        otherItem.setLabel( "Thing" );
                        otherItem.setPrice( "EUR 10" );
                        controller.addItem( cartId, otherItem );

                        items = List.of(item, otherItem);
                        controller.checkOutShoppingCart( cartId );
                    }


                    @Test
                    void shouldDeleteTheCart() {
                        assertThat( cartRepository.findAll() ).isEmpty();
                    }

                    @Test
                    void shouldCreateAnOrderWithACurrentTimestampAndOneAccumulatedPositionAndASumTotal() {
                        final Iterable<OrderEntity> orders = orderRepository.findAll();
                        assertThat( orders ).hasSize( 1 );
                        final OrderEntity order = orders.iterator().next();
                        assertThat(order.getPositions()).hasSize( 1 );
                        assertThat( order.getTimestamp().atOffset( ZoneOffset.ofHours( 1 ) ) ).isCloseToUtcNow( DATETIME_PRECISION );

                        final OrderPositionEntity position = order.getPositions().iterator().next();
                        assertThat( position.getItemName() ).isEqualTo( items.get( 0 ).getLabel());
                        assertThat( position.getItemName() ).isEqualTo( items.get( 1 ).getLabel());
                        assertThat( position.getCombinedPrice() ).isEqualTo( "EUR 20.00" );
                        assertThat( position.getSinglePrice() ).isEqualTo( "EUR 10.00" );

                        assertThat(order.getTotal()).isEqualTo( "EUR 20.00" );
                    }
                }
            }

            @Nested
            class AndTheCartDoesNoMatch {
                @Test
                void shouldThrow404NotFound() {
                    assertThatExceptionOfType( ResponseStatusException.class )
                        .isThrownBy( () -> controller.checkOutShoppingCart( UUID.randomUUID() ) )
                        .withMessageMatching(
                            "404 NOT_FOUND \"A shopping cart with id \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} does not exist.\"" );
                }
            }
        }


        @AfterEach
        public void tearDown() {
            cartRepository.deleteAll();
            orderRepository.deleteAll();
        }
    }
}
