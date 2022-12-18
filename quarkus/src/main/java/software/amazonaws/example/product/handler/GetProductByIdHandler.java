// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.util.Optional;

import javax.inject.Named;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.dao.ProductDao;
import software.amazonaws.example.product.entity.Product;

@Named("getProductById")
public class GetProductByIdHandler implements RequestHandler<APIGatewayProxyRequestEvent, Optional<Product>> {
	  private final ProductDao productDao = new DynamoProductDao();

	  @Override
	  public Optional<Product> handleRequest(APIGatewayProxyRequestEvent event, Context context) {
			String id = event.getPathParameters().get("id");
			Optional<Product> optionalProduct= productDao.getProduct(id);
			if(optionalProduct.isPresent()) context.getLogger().log(" product : "+optionalProduct.get());
			else context.getLogger().log(" product not found ");
			return optionalProduct;
	  }

}
