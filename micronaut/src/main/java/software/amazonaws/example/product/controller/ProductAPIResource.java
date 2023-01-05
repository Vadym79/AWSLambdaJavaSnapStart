package software.amazonaws.example.product.controller;

import org.crac.Context;
import org.crac.Resource;

import io.micronaut.crac.OrderedResource;
import jakarta.inject.Singleton;
import software.amazonaws.example.product.dao.ProductDao;


@Singleton
public class ProductAPIResource implements OrderedResource  {

	private final ProductDao productDao;
	
	public ProductAPIResource(ProductDao productDao) {
	    this.productDao = productDao;
	  }
	
    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
    	System.out.println("Before Checkpoint");
		productDao.getProduct("0");
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
    	System.out.println("After Restore");	
    }
}