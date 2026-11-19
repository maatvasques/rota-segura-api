package br.com.fiap.rotasegura.dto;

import br.com.fiap.rotasegura.model.Sensor;

public record SensorResponse(
        Long id,
        String codigo,
        String localizacao,
        String bairro,
        double limiteCm,
        boolean ativo
) {

    public static SensorResponse from(Sensor sensor) {
        return new SensorResponse(
                sensor.getId(),
                sensor.getCodigo(),
                sensor.getLocalizacao(),
                sensor.getBairro(),
                sensor.getLimiteCm(),
                sensor.isAtivo());
    }
}
