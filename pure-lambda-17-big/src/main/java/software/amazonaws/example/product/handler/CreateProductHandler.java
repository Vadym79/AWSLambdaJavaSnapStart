// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.http.HttpStatusCode;
import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.dao.ProductDao;
import software.amazonaws.example.product.entity.Product;

public class CreateProductHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private final ProductDao productDao = new DynamoProductDao();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
		try {
			String requestBody = requestEvent.getBody();
			Product product = objectMapper.readValue(requestBody, Product.class);
			productDao.putProduct(product);
			return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.CREATED)
					.withBody("Product with id = " + product.getId() + " created");
		} catch (Exception e) {
			e.printStackTrace();
			return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
					.withBody("Internal Server Error :: " + e.getMessage());
		}
	}
}