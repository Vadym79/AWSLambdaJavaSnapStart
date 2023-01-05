// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.util.Optional;

import org.crac.Resource;
import org.crac.Core;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.dao.ProductDao;
import software.amazonaws.example.product.entity.Product;

public class GetProductByIdWithPrimingHandler implements RequestHandler<APIGatewayProxyRequestEvent, Optional<Product>>, Resource {

	private static final ProductDao productDao = new DynamoProductDao();
	
	public GetProductByIdWithPrimingHandler () {
		Core.getGlobalContext().register(this);
	}

	@Override
	public Optional<Product> handleRequest(APIGatewayProxyRequestEvent event, Context context) {
		String id = event.getPathParameters().get("id");
		Optional<Product> optionalProduct = productDao.getProduct(id);
		if (optionalProduct.isPresent())
			context.getLogger().log(" product : " + optionalProduct.get());
		else
			context.getLogger().log(" product not found ");
		return optionalProduct;
	}

	@Override
	public void beforeCheckpoint(org.crac.Context<? extends Resource> context) throws Exception {
		System.out.println("Before Checkpoint");
		productDao.getProduct("0");
	}

	@Override
	public void afterRestore(org.crac.Context<? extends Resource> context) throws Exception {
		System.out.println("After Restore");	
	}
}
