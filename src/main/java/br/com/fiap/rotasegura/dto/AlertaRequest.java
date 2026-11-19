package br.com.fiap.rotasegura.dto;

import br.com.fiap.rotasegura.model.NivelAlerta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Alerta criado manualmente (ex: agente da Defesa Civil)
public record AlertaRequest(
        // Opcional: sensor relacionado ao alerta
        Long sensorId,

        @NotNull(message = "O nível do alerta é obrigatório (ATENCAO ou PERIGO)")
        NivelAlerta nivel,

        @NotBlank(message = "A mensagem é obrigatória")
        @Size(max = 300, message = "A mensagem deve ter no máximo 300 caracteres")
        String mensagem,

        @NotBlank(message = "O bairro é obrigatório")
        String bairro
) {
}
