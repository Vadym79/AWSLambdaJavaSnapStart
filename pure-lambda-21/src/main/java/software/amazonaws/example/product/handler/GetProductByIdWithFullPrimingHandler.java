// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.util.Map;
import java.util.Optional;

import org.crac.Core;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.serialization.events.LambdaEventSerializers;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.http.HttpStatusCode;
import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.dao.ProductDao;
import software.amazonaws.example.product.entity.Product;

public class GetProductByIdWithFullPrimingHandler implements 
                 RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, Resource {

	private static final ProductDao productDao = new DynamoProductDao();
	private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(GetProductByIdWithFullPrimingHandler.class);
	
	public GetProductByIdWithFullPrimingHandler () {
		Core.getGlobalContext().register(this);
	}
	
	@Override
	public void beforeCheckpoint(org.crac.Context<? extends Resource> context) throws Exception {
		logger.info("entered beforeCheckpoint");
		/*
		APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
		requestEvent.setPathParameters(Map.of("id","0"));
		*/
		//APIGatewayProxyRequestEvent requestEvent = LambdaEventSerializers.serializerFor(APIGatewayProxyRequestEvent.class, ClassLoader.getSystemClassLoader())
		//.fromJson(getAPIGatewayRequestMultiLine());
	    
		APIGatewayProxyRequestEvent requestEvent = LambdaEventSerializers.serializerFor(APIGatewayProxyRequestEvent.class, ClassLoader.getSystemClassLoader())
				.fromJson(objectMapper.writeValueAsString(getAwsProxyRequest()));
		logger.info("req event: "+requestEvent);	
		this.handleRequest(requestEvent, new MockLambdaContext());
		
    }

	private static String getAPIGatewayRequestMultiLine () {
		 return  """
		 		{
		          "httpMethod": "GET",
		          "pathParameters": {
		                "id": "0" 
		            }
		        }
	     """;
	}
	
    private static APIGatewayProxyRequestEvent getAwsProxyRequest () {
    	final APIGatewayProxyRequestEvent aPIGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent ();
    	aPIGatewayProxyRequestEvent.setHttpMethod("GET");
    	aPIGatewayProxyRequestEvent.setPathParameters(Map.of("id","0"));
        /*
    	aPIGatewayProxyRequestEvent.setPath("/products/0");
    	aPIGatewayProxyRequestEvent.setResource("/products/{id}");
    
    	final ProxyRequestContext proxyRequestContext = new ProxyRequestContext();
    	final RequestIdentity requestIdentity= new RequestIdentity();
    	requestIdentity.setApiKey("blabla");
    	proxyRequestContext.setIdentity(requestIdentity);
    	aPIGatewayProxyRequestEvent.setRequestContext(proxyRequestContext);
    	*/
    	return aPIGatewayProxyRequestEvent;		
    }
	
	@Override
	public void afterRestore(org.crac.Context<? extends Resource> context) throws Exception {	
	
	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
		String id = requestEvent.getPathParameters().get("id");
		Optional<Product> optionalProduct = productDao.getProduct(id);
		try {
			if (optionalProduct.isEmpty()) {
				context.getLogger().log(" product with id " + id + " not found ");
				return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.NOT_FOUND)
						.withBody("Product with id = " + id + " not found");
			}
			context.getLogger().log(" product " + optionalProduct.get() + " found ");
			return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.OK)
					.withBody(objectMapper.writeValueAsString(optionalProduct.get()));
		} catch (Exception je) {
			je.printStackTrace();
			return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
					.withBody("Internal Server Error :: " + je.getMessage());
		}
	}


}