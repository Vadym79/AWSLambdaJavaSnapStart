// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.math.BigDecimal;

import javax.inject.Named;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.dao.ProductDao;
import software.amazonaws.example.product.entity.Product;

@Named("putProduct")
public class CreateProductHandler implements RequestHandler<APIGatewayProxyRequestEvent, String> {
	  private final ProductDao productDao = new DynamoProductDao();

	  @Override
	  public String handleRequest(APIGatewayProxyRequestEvent event, Context context) {
		String id=event.getPathParameters().get("id");
		context.getLogger().log("create product with id "+id);
		Product product= new Product(id, "Calender ", BigDecimal.valueOf(35.99));
	    productDao.putProduct(product);
	    context.getLogger().log("created product : "+product);
	    return id;
	  }
 
}
