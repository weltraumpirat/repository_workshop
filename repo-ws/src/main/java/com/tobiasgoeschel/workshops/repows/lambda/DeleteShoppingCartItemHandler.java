package com.tobiasgoeschel.workshops.repows.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.tobiasgoeschel.workshops.repows.adapter.RemoveShoppingCartItemCommand;
import com.tobiasgoeschel.workshops.repows.lambda.persistence.ShoppingCartRepositoryDynamoDB;
import com.tobiasgoeschel.workshops.repows.usecase.RemoveShoppingCartItemCommandImpl;

import java.util.UUID;

@SuppressWarnings("unused")
public class DeleteShoppingCartItemHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final RemoveShoppingCartItemCommand command;

    /** Constructor for tests. Pass the mocked command. **/
    public DeleteShoppingCartItemHandler(final RemoveShoppingCartItemCommand command) {
        this.command = command;
    }

    /** Noargs constructor for Lambda invocation **/
    public DeleteShoppingCartItemHandler() {
        command = new RemoveShoppingCartItemCommandImpl(new ShoppingCartRepositoryDynamoDB());
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event, final Context context) {

        final UUID cartId = UUID.fromString(event.getPathParameters().get("cartId"));
        final UUID itemId = UUID.fromString(event.getPathParameters().get("itemId"));

        command.invoke(cartId, itemId);

        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        return response;
    }
}
