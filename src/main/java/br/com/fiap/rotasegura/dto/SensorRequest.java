package br.com.fiap.rotasegura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SensorRequest(
        @NotBlank(message = "O código é obrigatório")
        @Size(max = 20, message = "O código deve ter no máximo 20 caracteres")
        String codigo,

        @NotBlank(message = "A localização é obrigatória")
        String localizacao,

        @NotBlank(message = "O bairro é obrigatório")
        String bairro,

        @NotNull(message = "O limite em cm é obrigatório")
        @Positive(message = "O limite deve ser maior que zero")
        Double limiteCm,

        // Se não for enviado, o sensor é criado como ativo
        Boolean ativo
) {
}
