package no.brreg.createbranchbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Configuration
@EnableAsync
public class CreateBranchBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreateBranchBackendApplication.class, args);
	}

}
