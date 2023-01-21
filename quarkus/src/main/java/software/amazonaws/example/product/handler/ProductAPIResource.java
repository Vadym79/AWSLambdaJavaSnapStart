// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.crac.Core;
import org.crac.Resource;

import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyRequestContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler;
import io.quarkus.runtime.Startup;


@Startup
@ApplicationScoped
public class ProductAPIResource implements Resource {

	//private static final ProductDao productDao = new DynamoProductDao();
	
    @PostConstruct
	public void init () {
		Core.getGlobalContext().register(this);
	}

	@Override
	public void beforeCheckpoint(org.crac.Context<? extends Resource> context) throws Exception {
		System.out.println("Before Checkpoint");
		new QuarkusStreamHandler().handleRequest
		 (new ByteArrayInputStream(convertAwsProxRequestToJsonBytes()), new ByteArrayOutputStream(), new MockLambdaContext());
		//productDao.getProduct("0");
		System.out.println("After Checkpoint");
	}

	@Override
	public void afterRestore(org.crac.Context<? extends Resource> context) throws Exception {
		System.out.println("After Restore");	
	}
	
	/*
	private static String getAPIGatewayRequest () {
		StringBuilder sb = new StringBuilder();
	    sb.append("{\n")
		.append(" \"resource\": \"/products/{id}\",\n")
		.append("  \"path\": \"/products/0\",\n")
		.append("   \"httpMethod\": \"GET\",\n")
		.append("\"pathParameters\": {\n")
		.append("    \"id\":  \"0\"\n")
		.append("},\n")
		.append("  \"requestContext\": {\n")
		.append("     \"identity\": {\n")
	    .append("          \"apiKey\": \"blabla\"\n")
	    .append("      }\n")
		.append(" }\n")
		.append("}");
	    return sb.toString();
	}
	*/
	
	private static byte[] convertAwsProxRequestToJsonBytes () throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return ow.writeValueAsBytes(getAwsProxyRequest());
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