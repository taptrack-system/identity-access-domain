package com.identityaccessdomain.identityprofiles;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@OpenAPIDefinition(info = @Info(
  title = "Identity Profiles API", version = "1.0.0",
  description = "Centralized user roles and access management",
  contact = @Contact(name = "Juliane Maran", email = "julianemaran@gmail.com")),
  servers = @Server(url = "http://localhost:8081/identity-profiles"))
@SpringBootApplication
@EnableDiscoveryClient
public class IdentityProfilesApplication {

  private static final Logger log = LoggerFactory.getLogger(IdentityProfilesApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(IdentityProfilesApplication.class, args);
    log.info("Identity Profiles service started successfully!");
  }

}
