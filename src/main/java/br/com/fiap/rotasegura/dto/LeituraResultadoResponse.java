package br.com.fiap.rotasegura.dto;

import java.util.List;

// Resposta do POST /leituras
public record LeituraResultadoResponse(
        LeituraResponse leitura,
        AlertaResponse alerta,               // null quando não gerou alerta
        List<AbrigoResponse> abrigosDisponiveis
) {
}
