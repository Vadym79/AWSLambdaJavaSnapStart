// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import software.amazon.awssdk.services.rdsdata.RdsDataClient;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementRequest;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementResponse;
import software.amazon.awssdk.services.rdsdata.model.Field;
import software.amazon.awssdk.services.rdsdata.model.SqlParameter;
import software.amazonaws.example.product.entity.Product;

public class GetProductByIdViaServerlessV2DataAPIHandler implements RequestHandler<APIGatewayProxyRequestEvent, Optional<Product>> {

	private static final RdsDataClient rdsDataClient = RdsDataClient.builder().build();
	

	@Override 
	public Optional<Product> handleRequest(APIGatewayProxyRequestEvent event, Context context) {
		final String id = event.getPathParameters().get("id");
		final String dbEndpoint=System.getenv("DB_ENDPOINT");
		final String dbName=System.getenv("DB_NAME");
		final String dbClusterArn=System.getenv("DB_CLUSTER_ARN");
		final String dbSecretStoreArn=System.getenv("DB_CRED_SECRETS_STORE_ARN");
		
		final String sql="select id, name, price from products where id=:id";

		System.out.println("dbEndpoint: "+dbEndpoint+ " dbName: "+dbName+ " dbclusterARN: "+dbClusterArn+ " dbSecretStoreARN: "+dbSecretStoreArn);
		final SqlParameter sqlParam= SqlParameter.builder().name("id").value(Field.builder().longValue(Long.valueOf(id)).build()).build();
		System.out.println(" sql param "+sqlParam);
		final ExecuteStatementRequest request= ExecuteStatementRequest.builder().database("").
				resourceArn(dbClusterArn).
				secretArn(dbSecretStoreArn).
				sql(sql).
				parameters(sqlParam).
				//formatRecordsAs(RecordsFormatType.JSON).
				build();
		final ExecuteStatementResponse response= rdsDataClient.executeStatement(request);
		final List<List<Field>> records=response.records();
		
		if (records.isEmpty()) return Optional.empty();
		
		System.out.println("response records: "+records);
		
		final List<Field> fields= records.get(0);
		final String name= fields.get(1).stringValue(); 
		final BigDecimal price= new BigDecimal(fields.get(2).stringValue());
		final Product product = new Product(id, name, price);
		System.out.println("Product :"+product);
		
		return Optional.of(product);
	}

}
