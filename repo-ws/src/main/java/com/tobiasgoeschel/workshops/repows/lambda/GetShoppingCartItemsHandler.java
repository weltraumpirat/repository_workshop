package com.tobiasgoeschel.workshops.repows.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tobiasgoeschel.workshops.repows.adapter.ShoppingCartItemsQuery;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCartItem;
import com.tobiasgoeschel.workshops.repows.lambda.json.Mapper;
import com.tobiasgoeschel.workshops.repows.lambda.persistence.ShoppingCartRepositoryDynamoDB;
import com.tobiasgoeschel.workshops.repows.usecase.ShoppingCartItemsQueryImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class GetShoppingCartItemsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ShoppingCartItemsQuery query;

    /**
     * Constructor for tests. Pass the mocked query.
     **/
    public GetShoppingCartItemsHandler(final ShoppingCartItemsQuery query) {
        this.query = query;
    }

    /**
     * Noargs constructor for Lambda invocation.
     **/
    public GetShoppingCartItemsHandler() {
        this.query = new ShoppingCartItemsQueryImpl(new ShoppingCartRepositoryDynamoDB());
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event, final Context context) {


        final UUID cartId = UUID.fromString(event.getPathParameters().get("cartId"));
        final List<ShoppingCartItem> items = query.invoke(cartId);

        final String body = items.stream()
                .map((item) -> {
                    try {
                        return Mapper.create().writeValueAsString(item);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.joining(","));
        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setBody("[" + body + "]");
        response.setStatusCode(200);
        return response;
    }
}
