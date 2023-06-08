package com.tobiasgoeschel.workshops.repows.usecase;

import com.tobiasgoeschel.workshops.repows.adapter.CheckOutShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.CheckOutService;
import com.tobiasgoeschel.workshops.repows.domain.Order;

import java.util.UUID;

public class CheckOutShoppingCartCommandImpl implements CheckOutShoppingCartCommand {
    private final CheckOutService service;

    public CheckOutShoppingCartCommandImpl( final CheckOutService service ) {
        this.service = service;
    }


    @Override public UUID invoke( final UUID cartId ) {
        final Order order = service.checkOut( cartId );
        return order.getId();
    }
}
