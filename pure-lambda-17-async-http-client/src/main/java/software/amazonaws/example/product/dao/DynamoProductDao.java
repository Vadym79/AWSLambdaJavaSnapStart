// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package software.amazonaws.example.product.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazonaws.example.product.entity.Product;
import software.amazonaws.example.product.entity.Products;

public class DynamoProductDao implements ProductDao {
	private static final Logger logger = LoggerFactory.getLogger(DynamoProductDao.class);
	private static final String PRODUCT_TABLE_NAME = System.getenv("PRODUCT_TABLE_NAME");

	private static final DynamoDbAsyncClient dynamoDbClient = DynamoDbAsyncClient.builder()
			.credentialsProvider(DefaultCredentialsProvider.create()).region(Region.EU_CENTRAL_1)
			//.httpClient(AwsCrtAsyncHttpClient.create())
			 .httpClient(NettyNioAsyncHttpClient.create())
			.build();

	@Override
	public Optional<Product> getProduct(String id) {
		CompletableFuture<GetItemResponse> getItemReponseAsync = dynamoDbClient.getItem(GetItemRequest.builder()
				.key(Map.of("PK", AttributeValue.builder().s(id).build())).tableName(PRODUCT_TABLE_NAME).build());
		GetItemResponse getItemResponse = getItemReponseAsync.join();
		if (getItemResponse.hasItem()) {
			return Optional.of(ProductMapper.productFromDynamoDB(getItemResponse.item()));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void putProduct(Product product) {
		dynamoDbClient.putItem(PutItemRequest.builder().tableName(PRODUCT_TABLE_NAME)
				.item(ProductMapper.productToDynamoDb(product)).build()).join();
	}

	@Override
	public void deleteProduct(String id) {
		dynamoDbClient.deleteItem(DeleteItemRequest.builder().tableName(PRODUCT_TABLE_NAME)
				.key(Map.of("PK", AttributeValue.builder().s(id).build())).build());
	}

	@Override
	public Products getAllProduct() {
		ScanResponse scanResponse = dynamoDbClient
				.scan(ScanRequest.builder().tableName(PRODUCT_TABLE_NAME).limit(20).build()).join();
		List<Product> productList = new ArrayList<>();
		for (Map<String, AttributeValue> item : scanResponse.items()) {
			productList.add(ProductMapper.productFromDynamoDB(item));
		}
		return new Products(productList);
	}
}