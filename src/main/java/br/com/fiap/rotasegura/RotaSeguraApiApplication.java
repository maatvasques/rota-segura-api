package br.com.fiap.rotasegura;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "Rota Segura API",
        version = "1.0",
        description = "Sistema de alerta de enchentes - Global Solution FIAP (ODS 11 e 13)"))
@SpringBootApplication
public class RotaSeguraApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RotaSeguraApiApplication.class, args);
    }
}
