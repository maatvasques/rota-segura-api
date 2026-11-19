package br.com.fiap.rotasegura.dto;

import br.com.fiap.rotasegura.model.LeituraSensor;

import java.time.LocalDateTime;

public record LeituraResponse(
        Long id,
        String codigoSensor,
        double nivelCm,
        double chuvaMm,
        LocalDateTime dataHora
) {

    public static LeituraResponse from(LeituraSensor leitura) {
        return new LeituraResponse(
                leitura.getId(),
                leitura.getSensor().getCodigo(),
                leitura.getNivelCm(),
                leitura.getChuvaMm(),
                leitura.getDataHora());
    }
}
