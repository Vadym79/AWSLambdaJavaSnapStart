// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.controller;

import java.util.Optional;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import software.amazonaws.example.product.dao.ProductDao;
import software.amazonaws.example.product.entity.Product;

@Controller
public class GetProductByIdController {

  private final ProductDao productDao;

  public GetProductByIdController(ProductDao productDao) {
    this.productDao = productDao;
  }
 
  @Get("/products/{id}")
  public Optional<Product> getProductById(@PathVariable String id) {
	Optional<Product> optionalProduct = productDao.getProduct(id);
	if (optionalProduct.isPresent())
		System.out.println(" product : " + optionalProduct.get());
	else
		System.out.println(" product not found ");
	return optionalProduct;
  }

}
