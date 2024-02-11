package software.amazonaws.example.product.controller;

import org.crac.Context;
import org.crac.Resource;

import io.micronaut.crac.OrderedResource;
import jakarta.inject.Singleton;
import software.amazonaws.example.product.dao.ProductDao;


@Singleton
public class ProductAPIResourceWithPriming implements OrderedResource  {

	private final ProductDao productDao;
	
	
	public ProductAPIResourceWithPriming(ProductDao productDao) {
	    this.productDao = productDao;
	}

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
    	this.productDao.getProduct("0");
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
    }
}
