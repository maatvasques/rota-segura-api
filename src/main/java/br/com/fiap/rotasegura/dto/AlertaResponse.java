package br.com.fiap.rotasegura.dto;

import br.com.fiap.rotasegura.model.Alerta;
import br.com.fiap.rotasegura.model.NivelAlerta;

import java.time.LocalDateTime;

public record AlertaResponse(
        Long id,
        NivelAlerta nivel,
        String mensagem,
        String bairro,
        LocalDateTime dataHora,
        boolean ativo,
        String codigoSensor,
        Long leituraId
) {

    public static AlertaResponse from(Alerta alerta) {
        // Alerta manual pode não ter sensor nem leitura
        String codigoSensor = alerta.getSensor() != null ? alerta.getSensor().getCodigo() : null;
        Long leituraId = alerta.getLeitura() != null ? alerta.getLeitura().getId() : null;

        return new AlertaResponse(
                alerta.getId(),
                alerta.getNivel(),
                alerta.getMensagem(),
                alerta.getBairro(),
                alerta.getDataHora(),
                alerta.isAtivo(),
                codigoSensor,
                leituraId);
    }
}
