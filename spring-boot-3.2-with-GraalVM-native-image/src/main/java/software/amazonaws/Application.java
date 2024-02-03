package software.amazonaws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import software.amazonaws.example.product.controller.ProductController;

@Import({ ProductController.class })
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })

public class Application {

	public static void main(String[] args) {
		//SpringApplication.run(Application.class, args);
		Companion.main(args);
	}

	static class Companion {

		static void main(String[] args) {
			SpringApplication springApplication = new SpringApplication(Application.class);
			springApplication.setMainApplicationClass(Application.class);
			springApplication.run(args);
		}

	}
}