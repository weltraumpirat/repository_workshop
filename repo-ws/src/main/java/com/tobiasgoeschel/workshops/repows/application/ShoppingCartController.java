package com.tobiasgoeschel.workshops.repows.application;

import com.tobiasgoeschel.workshops.repows.adapter.*;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItem;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItemFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin( origins = { "http://localhost:3000", "http://localhost:3001", "http://localhost" } )

@RestController
public class ShoppingCartController {
    private final ShoppingCartsQuery shoppingCartsQuery;
    private final ShoppingCartItemsQuery shoppingCartItemsQuery;
    private final CreateEmptyShoppingCartCommand createEmptyShoppingCartCommand;
    private final DeleteShoppingCartCommand deleteShoppingCartCommand;
    private final AddShoppingCartItemCommand addShoppingCartItemCommand;
    private final RemoveShoppingCartItemCommand removeShoppingCartItemCommand;
    private final CheckOutShoppingCartCommand checkOutShoppingCartCommand;

    public ShoppingCartController( final ShoppingCartsQuery shoppingCartsQuery,
                                   final ShoppingCartItemsQuery shoppingCartItemsQuery,
                                   final CreateEmptyShoppingCartCommand createEmptyShoppingCartCommand,
                                   final DeleteShoppingCartCommand deleteShoppingCartCommand,
                                   final AddShoppingCartItemCommand addShoppingCartItemCommand,
                                   final RemoveShoppingCartItemCommand removeShoppingCartItemCommand,
                                   final CheckOutShoppingCartCommand checkOutShoppingCartCommand ) {
        this.shoppingCartsQuery = shoppingCartsQuery;
        this.shoppingCartItemsQuery = shoppingCartItemsQuery;
        this.createEmptyShoppingCartCommand = createEmptyShoppingCartCommand;
        this.deleteShoppingCartCommand = deleteShoppingCartCommand;
        this.addShoppingCartItemCommand = addShoppingCartItemCommand;
        this.removeShoppingCartItemCommand = removeShoppingCartItemCommand;
        this.checkOutShoppingCartCommand = checkOutShoppingCartCommand;
    }


    @GetMapping( "/api/cart" )
    public List<ShoppingCart> getCarts() {
        return shoppingCartsQuery.invoke();
    }

    @GetMapping( "/api/cart/{cartId}" )
    public List<ShoppingCartItem> getCartItems( @PathVariable final UUID cartId ) {
        return shoppingCartItemsQuery.invoke(cartId);
    }

    @PostMapping( "/api/cart" )
    public ShoppingCart createEmptyCart() {
        return createEmptyShoppingCartCommand.invoke();
    }

    @DeleteMapping( "/api/cart/{cartId}" )
    @Transactional
    public void deleteCart( @PathVariable final UUID cartId ) {
        deleteShoppingCartCommand.invoke(cartId);
    }

    @PostMapping( "/api/cart/{cartId}" )
    public void addItem( @PathVariable final UUID cartId, @RequestBody final ShoppingCartItem item ) {
        ShoppingCartItem itemWithNewId = ShoppingCartItemFactory.create(item.getLabel(), item.getPrice());
        addShoppingCartItemCommand.invoke(cartId, item.getId() != null ? item : itemWithNewId);
    }

    @DeleteMapping( "/api/cart/{cartId}/{itemId}" )
    public void removeItem( @PathVariable final UUID cartId, @PathVariable UUID itemId ) {
        removeShoppingCartItemCommand.invoke( cartId, itemId );
    }

    @PostMapping( "/api/cart/{cartId}/checkout" )
    @Transactional
    public UUID checkOutShoppingCart( @PathVariable final UUID cartId ) {
        return checkOutShoppingCartCommand.invoke(cartId);
   }
}
