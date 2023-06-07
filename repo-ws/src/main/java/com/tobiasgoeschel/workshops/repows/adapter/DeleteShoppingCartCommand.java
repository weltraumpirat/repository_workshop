package com.tobiasgoeschel.workshops.repows.adapter;

import java.util.UUID;

public interface DeleteShoppingCartCommand {
    void invoke( UUID cartId );
}
