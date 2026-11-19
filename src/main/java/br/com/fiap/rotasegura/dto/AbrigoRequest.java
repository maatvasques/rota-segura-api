package br.com.fiap.rotasegura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record AbrigoRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @NotBlank(message = "O bairro é obrigatório")
        String bairro,

        @NotNull(message = "A capacidade é obrigatória")
        @Positive(message = "A capacidade deve ser maior que zero")
        Integer capacidade,

        @NotNull(message = "As vagas disponíveis são obrigatórias")
        @PositiveOrZero(message = "As vagas disponíveis não podem ser negativas")
        Integer vagasDisponiveis,

        @NotNull(message = "A latitude é obrigatória")
        Double latitude,

        @NotNull(message = "A longitude é obrigatória")
        Double longitude,

        // Se não for enviado, o abrigo é criado como ativo
        Boolean ativo
) {
}
