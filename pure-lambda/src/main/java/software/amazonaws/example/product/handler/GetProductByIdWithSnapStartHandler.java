// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.util.Optional;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.dao.ProductDao;
import software.amazonaws.example.product.entity.Product;


public class GetProductByIdWithSnapStartHandler implements RequestHandler<Object, Product> {
	
  private static final ProductDao productDao = new DynamoProductDao();
  
  @Override
  public Product handleRequest(Object object, Context context) {
	
	  context.getLogger().log(" id "+(Integer)object);
	  
	  Optional<Product> optionalProduct= productDao.getProduct(String.valueOf(object));
	  context.getLogger().log("created product is: "+optionalProduct);
	  if (optionalProduct.isPresent()) return optionalProduct.get(); 
	  else return null;
  }

}
