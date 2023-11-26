package com.tobiasgoeschel.workshops.repows.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.tobiasgoeschel.workshops.repows.adapter.CreateEmptyShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.lambda.persistence.ShoppingCartRepositoryDynamoDB;
import com.tobiasgoeschel.workshops.repows.usecase.CreateEmptyShoppingCartCommandImpl;

@SuppressWarnings("unused")
public class PostShoppingCartHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final CreateEmptyShoppingCartCommand command;

    /** Constructor for tests. Pass the mocked command. **/
    public PostShoppingCartHandler(final CreateEmptyShoppingCartCommand command) {
        this.command = command;
    }

    /** Noargs constructor for Lambda invocation.**/
    public PostShoppingCartHandler() {
        this.command = new CreateEmptyShoppingCartCommandImpl(new ShoppingCartRepositoryDynamoDB());
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event, final Context context) {

        final ShoppingCart cart = command.invoke();

        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setBody(cart.getId().toString());
        return response;
    }
}
