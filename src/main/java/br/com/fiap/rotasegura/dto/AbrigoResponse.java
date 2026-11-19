package br.com.fiap.rotasegura.dto;

import br.com.fiap.rotasegura.model.Abrigo;

public record AbrigoResponse(
        Long id,
        String nome,
        String endereco,
        String bairro,
        int capacidade,
        int vagasDisponiveis,
        Double latitude,
        Double longitude,
        boolean ativo
) {

    public static AbrigoResponse from(Abrigo abrigo) {
        return new AbrigoResponse(
                abrigo.getId(),
                abrigo.getNome(),
                abrigo.getEndereco(),
                abrigo.getBairro(),
                abrigo.getCapacidade(),
                abrigo.getVagasDisponiveis(),
                abrigo.getLatitude(),
                abrigo.getLongitude(),
                abrigo.isAtivo());
    }
}
