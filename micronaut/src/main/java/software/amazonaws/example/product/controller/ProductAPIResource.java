package software.amazonaws.example.product.controller;

import java.util.Map;

import org.crac.Context;
import org.crac.Resource;

import io.micronaut.crac.OrderedResource;
import io.micronaut.function.aws.proxy.MicronautLambdaHandler;
import jakarta.inject.Singleton;

import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyRequestContext;

@Singleton
public class ProductAPIResource implements OrderedResource  {

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
    	System.out.println("Before Checkpoint");
    	try (MicronautLambdaHandler micronautLambdaHandler = new MicronautLambdaHandler()) {
			micronautLambdaHandler.handleRequest(getAwsProxyRequest(), new MockLambdaContext());
		}
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
    }
    
    private static AwsProxyRequest getAwsProxyRequest () {
    	final AwsProxyRequest awsProxyRequest = new AwsProxyRequest ();
    	awsProxyRequest.setHttpMethod("GET");
    	awsProxyRequest.setPath("/products/0");
    	awsProxyRequest.setResource("/products/{id}");
    	awsProxyRequest.setPathParameters(Map.of("id","0"));
    	final AwsProxyRequestContext awsProxyRequestContext = new AwsProxyRequestContext();
    	final ApiGatewayRequestIdentity apiGatewayRequestIdentity= new ApiGatewayRequestIdentity();
    	apiGatewayRequestIdentity.setApiKey("blabla");
    	awsProxyRequestContext.setIdentity(apiGatewayRequestIdentity);
    	awsProxyRequest.setRequestContext(awsProxyRequestContext);
    	return awsProxyRequest;		
    }
}
