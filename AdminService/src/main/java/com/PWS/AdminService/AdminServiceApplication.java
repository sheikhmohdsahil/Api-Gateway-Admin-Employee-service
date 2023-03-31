package com.PWS.AdminService;

import com.PWS.AdminService.Utility.AuditAwareImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@OpenAPIDefinition
@ComponentScan(basePackages = {"com.PWS.AdminService.*"})
public class AdminServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware(){return new AuditAwareImpl();
	}

}
