package br.com.fiap.rotasegura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record LeituraRequest(
        @NotBlank(message = "O código do sensor é obrigatório")
        String codigoSensor,

        @NotNull(message = "O nível em cm é obrigatório")
        @PositiveOrZero(message = "O nível não pode ser negativo")
        Double nivelCm,

        @NotNull(message = "A chuva em mm é obrigatória")
        @PositiveOrZero(message = "A chuva não pode ser negativa")
        Double chuvaMm
) {
}
