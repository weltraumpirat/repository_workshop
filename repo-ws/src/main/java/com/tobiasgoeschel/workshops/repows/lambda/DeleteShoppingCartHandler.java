package com.tobiasgoeschel.workshops.repows.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.tobiasgoeschel.workshops.repows.adapter.DeleteShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.lambda.persistence.ShoppingCartRepositoryDynamoDB;
import com.tobiasgoeschel.workshops.repows.usecase.DeleteShoppingCartCommandImpl;

import java.util.UUID;

@SuppressWarnings("unused")
public class DeleteShoppingCartHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final DeleteShoppingCartCommand command;

    /** Constructor for tests. Pass the mocked command. **/
    public DeleteShoppingCartHandler(final DeleteShoppingCartCommand command) {
        this.command = command;
    }

    /** Noargs constructor for Lambda invocation **/
    public DeleteShoppingCartHandler() {
        command = new DeleteShoppingCartCommandImpl(new ShoppingCartRepositoryDynamoDB());
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event, final Context context) {

        final UUID cartId = UUID.fromString(event.getPathParameters().get("cartId"));

        command.invoke(cartId);

        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        return response;
    }
}