// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import org.crac.Core;
import org.crac.Resource;

import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.dao.ProductDao;

import io.quarkus.runtime.Startup;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class ProductAPIResource implements Resource {

	private static final ProductDao productDao = new DynamoProductDao();
	
    @PostConstruct
	public void init () {
		Core.getGlobalContext().register(this);
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