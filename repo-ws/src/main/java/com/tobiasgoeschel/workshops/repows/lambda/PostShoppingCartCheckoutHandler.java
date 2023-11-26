package com.tobiasgoeschel.workshops.repows.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.tobiasgoeschel.workshops.repows.adapter.CheckOutShoppingCartCommand;
import com.tobiasgoeschel.workshops.repows.domain.CheckOutService;
import com.tobiasgoeschel.workshops.repows.lambda.persistence.OrderRepositoryDynamoDB;
import com.tobiasgoeschel.workshops.repows.lambda.persistence.ShoppingCartRepositoryDynamoDB;
import com.tobiasgoeschel.workshops.repows.usecase.CheckOutShoppingCartCommandImpl;

import java.util.UUID;

@SuppressWarnings("unused")
public class PostShoppingCartCheckoutHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final CheckOutShoppingCartCommand command;

    /** Constructor for tests. Pass the mocked command. **/
    public PostShoppingCartCheckoutHandler(final CheckOutShoppingCartCommand command) {
        this.command = command;
    }

    /** Noargs constructor for Lambda invocation **/
    public PostShoppingCartCheckoutHandler() {
        command = new CheckOutShoppingCartCommandImpl(new CheckOutService(new ShoppingCartRepositoryDynamoDB(), new OrderRepositoryDynamoDB()));
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event, final Context context) {

        final UUID cartId = UUID.fromString(event.getPathParameters().get("cartId"));

        final UUID orderId = command.invoke(cartId);

        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setBody(orderId.toString());
        return response;
    }
}
