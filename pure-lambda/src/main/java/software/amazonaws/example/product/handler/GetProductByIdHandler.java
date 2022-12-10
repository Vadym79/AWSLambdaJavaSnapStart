// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

public class GetProductByIdHandler implements RequestHandler<APIGatewayProxyRequestEvent, Void> {

	private final LambdaClient lambdaClient = LambdaClient.builder()
			.region(Region.EU_CENTRAL_1)
			.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
			.httpClientBuilder(UrlConnectionHttpClient.builder()).build();

	@Override
	public Void handleRequest(APIGatewayProxyRequestEvent event, Context context) {
		String id = event.getPathParameters().get("id");
		context.getLogger().log("get product with id " + id);

		SdkBytes payload = SdkBytes.fromUtf8String(id);
		InvokeRequest invokeRequest = InvokeRequest.builder()
				.functionName("GetProductByIdWithSnapStart")
				.qualifier("7")
				.payload(payload).build();
		context.getLogger().log("before calling GetProductByIdWithSnapStart Lambda function ");
		InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
		context.getLogger().log("Response: " + invokeResponse.toString());
		context.getLogger().log("Response Payload: " + invokeResponse.payload().asUtf8String());
        
		return null;
	}

}
