package com.identityaccessdomain.userservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@OpenAPIDefinition(
  info = @Info(
    title = "User Service API",
    version = "1.0.0",
    description = "API de gerenciamento de usuários (CRUD) do User Service."
  ),
  servers = @Server(
    url = "http://localhost:8081/api/v1",
    description = "Local environment"
  )
)
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class UserServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserServiceApplication.class, args);
  }

}
