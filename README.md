# Explore ways to use Java Runtimes with AWS Lambda with and without AWS Lambda SnapStart  using different framewoks. 

## Architecture

<p align="center">
  <img src="pure-lambda-21/src/main/resources/img/app_arch.png" alt="Application Architecture"/>
</p>

## Project Description
The code example include storing and retrieving product from the Amazon DynamoDB. I put Amazon API Gateway in front of my Lambdas.

I made all the test for the following use cases:  

- Lambda function without SnapStart enabled  
- Lambda function with SnapStart enabled but without usage of Priming
- Lambda function with SnapStart enabled but with usage of Priming (DynamoDB request invocation or proxing the whole web request)      

I tested different impact of the Lambda cold and warm start of the following:   

- Deployment package sizes  
- Lambda memory settings  
- Java compilation options   
- Synchronous HTTP clients (Url Connection, Apache, synchronous AWS CRT)  
- Asynchronous HTTP clients (Netty NIO, asynchronous AWS CRT)  

To the explored AWS Lambda Java Runtimes belong:  
- Java 11  
- Java 17  
- Java 21  

To the explore frameworks belong :  
- Micronaut
- Quarkus 
- Spring Boot (including Spring Boot 3.2. version)

# Installation and deployment

```bash

Clone git repository locally
git clone https://github.com/Vadym79/AWSLambdaJavaSnapStart.git

Compile and package the Java application with Maven from the root (where pom.xml is located) of the project
mvn clean package

Deploy your application with AWS SAM
sam deploy -g  
```

In order not to use AWS Lambda SnapStart comment both lines in the globals's section of the Lambda function.

Globals:  
  Function:  
     #SnapStart:  
       #ApplyOn: PublishedVersions   

In order to user AWS Lambda SnapStart uncomment both lines above. For different Priming optimizations enabling of SnapStart is required



## Further Readings 

You can read my article series [AWS Lambda SnapStart](https://dev.to/vkazulkin/measuring-java-11-lambda-cold-starts-with-snapstart-part-1-first-impressions-30a4)
