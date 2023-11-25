// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazonaws.example.product.entity.Product;

public class GetProductByIdHandler implements RequestHandler<Map<Object,Object>, Optional<Product>> {



	@Override
	public Optional<Product> handleRequest(Map<Object,Object> map, Context context) {
		context.getLogger().log("context map "+map);
		Optional<Product> optionalProduct = Optional.of(new Product ("2", "Calendar", BigDecimal.TEN));
		return optionalProduct;
	}

}
