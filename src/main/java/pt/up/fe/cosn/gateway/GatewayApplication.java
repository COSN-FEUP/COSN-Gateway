package pt.up.fe.cosn.gateway;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(new Info().title("COSN API Gateway").version("1.0.0"));
                /*.addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new Components().addSecuritySchemes("Authorization", new SecurityScheme().name("Authorization").type(SecurityScheme.Type.HTTP)
                        .scheme("bearer").bearerFormat("JWT")));*/
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }



}
