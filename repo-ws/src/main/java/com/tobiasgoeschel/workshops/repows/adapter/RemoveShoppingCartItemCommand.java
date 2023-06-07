package com.tobiasgoeschel.workshops.repows.adapter;

import java.util.UUID;

public interface RemoveShoppingCartItemCommand {
    void invoke( UUID shoppingCartId, UUID itemId );
}
