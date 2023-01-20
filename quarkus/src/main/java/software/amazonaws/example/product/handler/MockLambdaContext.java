package software.amazonaws.example.product.handler;

import java.nio.charset.Charset;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;

public class MockLambdaContext implements Context {

	 private static class MockLambdaConsoleLogger implements LambdaLogger {

	        //-------------------------------------------------------------
	        // Implementation - LambdaLogger
	        //-------------------------------------------------------------

	        @Override
	        public void log(String s) {
	            System.out.println(s);
	        }


	        @Override
	        public void log(byte[] bytes) {
	            System.out.println(new String(bytes, Charset.defaultCharset()));
	        }
	    }
	
    //-------------------------------------------------------------
    // Variables - Private - Static
    //-------------------------------------------------------------

    private static LambdaLogger logger = new MockLambdaConsoleLogger();


    //-------------------------------------------------------------
    // Implementation - Context
    //-------------------------------------------------------------


    @Override
    public String getAwsRequestId() {
        return null;
    }


    @Override
    public String getLogGroupName() {
        return null;
    }


    @Override
    public String getLogStreamName() {
        return null;
    }


    @Override
    public String getFunctionName() {
        return null;
    }


    @Override
    public String getFunctionVersion() {
        return null;
    }


    @Override
    public String getInvokedFunctionArn() {
        return null;
    }


    @Override
    public CognitoIdentity getIdentity() {
        return null;
    }


    @Override
    public ClientContext getClientContext() {
        return null;
    }


    @Override
    public int getRemainingTimeInMillis() {
        return 0;
    }


    @Override
    public int getMemoryLimitInMB() {
        return 0;
    }


    @Override
    public LambdaLogger getLogger() {
        return logger;
    }

} 