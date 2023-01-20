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
    
	//private static final ProductDao productDao = new DynamoProductDao();
	
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
		 logger.info("Before Checkpoint");
		
		 handler.proxyStream(new ByteArrayInputStream(getAPIGatewayRequest().getBytes(StandardCharsets.UTF_8)), 
				 new ByteArrayOutputStream(), new MockLambdaContext());
		//productDao.getProduct("0");
		 logger.info("After Checkpoint"); 
	}

	@Override
	public void afterRestore(org.crac.Context<? extends Resource> context) throws Exception {
		logger.info("After Restore");	
	}
	
	private static String getAPIGatewayRequest () {
		StringBuilder sb = new StringBuilder();
	    sb.append("{\n")
		.append(" \"resource\": \"/products/{id}\",\n")
		.append("  \"path\": \"/products/0\",\n")
		.append("   \"httpMethod\": \"GET\",\n")
		.append("  \"requestContext\": {\n")
		.append("     \"identity\": {\n")
	    .append("          \"apiKey\": \"blabla\"\n")
	    .append("      }\n")
		.append(" }\n")
		.append("}");
	    return sb.toString();
	}
	
}