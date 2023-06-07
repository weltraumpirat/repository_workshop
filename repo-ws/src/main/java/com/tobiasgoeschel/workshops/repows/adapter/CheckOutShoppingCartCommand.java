package com.tobiasgoeschel.workshops.repows.adapter;

import java.util.UUID;

public interface CheckOutShoppingCartCommand {
    UUID invoke( UUID cartId );
}
