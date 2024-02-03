package software.amazonaws;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import software.amazonaws.example.product.controller.ProductController;
import software.amazonaws.example.product.handler.CreateProductHandler;
import software.amazonaws.example.product.handler.GetProductByIdHandler;



@Import({ProductController.class })
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})

public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean  
    public GetProductByIdHandler getProductById () {
    	return new GetProductByIdHandler();
    }
    
    @Bean  
    public CreateProductHandler createProduct () {
    	return new CreateProductHandler();
    }
}