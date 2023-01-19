package software.amazonaws.example.product.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.crac.Core;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import software.amazonaws.Application;
import software.amazonaws.example.product.dao.DynamoProductDao;
import software.amazonaws.example.product.dao.ProductDao;


public class StreamLambdaHandler implements RequestStreamHandler, Resource {
	
	
	private static final Logger logger = LoggerFactory.getLogger(StreamLambdaHandler.class);
	
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
            // For applications that take longer than 10 seconds to start, use the async builder:
            // handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
            //                    .defaultProxy()
            //                    .asyncInit()
            //                    .springBootApplication(Application.class)
            //                    .buildAndInitialize();
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }
    
	private static final ProductDao productDao = new DynamoProductDao();
	
	public StreamLambdaHandler () {
		Core.getGlobalContext().register(this);
	}


    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
    	
    	logger.info("entered generic stream lambda handler");
        handler.proxyStream(inputStream, outputStream, context);
    }
    
	@Override
	public void beforeCheckpoint(org.crac.Context<? extends Resource> context) throws Exception {
		System.out.println("Before Checkpoint");
		
		 handler.proxyStream(new ByteArrayInputStream(getApiGateWayRequest().getBytes(StandardCharsets.UTF_8)), 
				 new ByteArrayOutputStream(), new MockLambdaContext());
		//productDao.getProduct("0");
		 System.out.println("After Checkpoint"); 
	}

	@Override
	public void afterRestore(org.crac.Context<? extends Resource> context) throws Exception {
		System.out.println("After Restore");	
	}
	
	private static String getApiGateWayRequest () {
		StringBuilder sb = new StringBuilder();
	       sb.append("{\n")
		  .append(" \"resource\": \"/products/{id}\",\n")
		  .append("  \"path\": \"/products/0\",\n")
		  .append("   \"httpMethod\": \"GET\",\n")
		   /*
		  .append("  \"headers\": {},\n")
		  .append("  \"multiValueHeaders\": {     },\n")
		  .append("  \"queryStringParameters\": null,\n")
		  .append("  \"multiValueQueryStringParameters\": null,\n")
		  .append("  \"pathParameters\": {\n")
		  .append("      \"id\": \"0\"\n")
		  .append(" },\n")
		  */
		  //.append("  \"stageVariables\": null,\n")
		  .append("  \"requestContext\": {\n")
		  //.append("      \"resourceId\": \"sd5yqu\",\n")
		  //.append("      \"resourcePath\": \"/products/{id}\",\n")
		  //.append("      \"httpMethod\": \"GET\",\n")
		  //.append("     \"extendedRequestId\": \"e5GtaFx1FiAFqMg=\",\n")
		  //.append("     \"requestTime\": \"17/Jan/2023:14:59:59 +0000\",\n")
		  //.append("      \"path\":  \"/prod/products/1\",\n")
		  //.append("      \"accountId\": \"265634257610\",\n")
		  //.append("      \"protocol\": \"HTTP/1.1\",\n")
		  //.append("      \"stage\": \"prod\",\n")
		  //.append("      \"domainPrefix\": \"4v2p3yxoph\",\n")
		  //.append("     \"requestTimeEpoch\": 1673967599229,\n")
		  //.append("     \"requestId\": \"fae79bb6-faee-4bb4-abfd-1f0e5b20625a\",\n")
		  .append("     \"identity\": {\n")
	      //.append("         \"cognitoIdentityPoolId\": null,\n")
	      //.append("          \"cognitoIdentityId\": null,\n")
	      .append("          \"apiKey\": \"a6ZbcDefQW12BN56WEM2\"\n")
	      //.append("          \"principalOrgId\": null,\n")
	      //.append("          \"cognitoAuthenticationType\": null,\n")
	      //.append("         \"userArn\": null,\n")
	      //.append("         \"apiKeyId\": \"jatl0igdai\",\n")
	      //.append("         \"userAgent\": \"Apache-HttpClient/4.5.13 (Java/16.0.1)\",\n")
	      //.append("         \"accountId\": null,\n")
	      //.append("         \"caller\": null,\n")
	      //.append("         \"sourceIp\": \"78.94.38.106\",\n")
	      //.append("        \"accessKey\": null,\n")
	      //.append("        \"cognitoAuthenticationProvider\": null,\n")
	      //.append("        \"user\": null\n")
	      .append("      }\n")
		  //.append("      \"domainName\": \"4v2p3yxoph.execute-api.eu-central-1.amazonaws.com\",\n")
		  //.append("      \"apiId\": \"4v2p3yxoph\"\n")
		  .append(" }\n")
		  //.append("  \"body\": null,\n")
		  //.append("  \"isBase64Encoded\": false\n")
		  .append("}");
	      return sb.toString();
	}
	
}