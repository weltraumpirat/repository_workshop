package com.tobiasgoeschel.workshops.repows.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tobiasgoeschel.workshops.repows.adapter.ShoppingCartsQuery;
import com.tobiasgoeschel.workshops.repows.domain.ShoppingCart;
import com.tobiasgoeschel.workshops.repows.lambda.json.Mapper;
import com.tobiasgoeschel.workshops.repows.lambda.persistence.ShoppingCartRepositoryDynamoDB;
import com.tobiasgoeschel.workshops.repows.usecase.ShoppingCartsQueryImpl;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class GetShoppingCartsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ShoppingCartsQuery query;

    /**
     * Constructor for tests. Pass the mocked query.
     **/
    public GetShoppingCartsHandler(final ShoppingCartsQuery query) {
        this.query = query;
    }

    /**
     * Noargs constructor for Lambda invocation.
     **/
    public GetShoppingCartsHandler() {
        this.query = new ShoppingCartsQueryImpl(new ShoppingCartRepositoryDynamoDB());
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event, final Context context) {

        final List<ShoppingCart> carts = query.invoke();

        final String body = carts.stream()
                .map((cart) -> {
                    try {
                        return Mapper.create().writeValueAsString(cart);
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
