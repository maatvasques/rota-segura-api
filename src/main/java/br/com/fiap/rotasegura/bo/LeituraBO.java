package br.com.fiap.rotasegura.bo;

import br.com.fiap.rotasegura.dto.AbrigoResponse;
import br.com.fiap.rotasegura.dto.AlertaResponse;
import br.com.fiap.rotasegura.dto.LeituraRequest;
import br.com.fiap.rotasegura.dto.LeituraResponse;
import br.com.fiap.rotasegura.dto.LeituraResultadoResponse;
import br.com.fiap.rotasegura.exception.RecursoNaoEncontradoException;
import br.com.fiap.rotasegura.exception.RegraNegocioException;
import br.com.fiap.rotasegura.model.Alerta;
import br.com.fiap.rotasegura.model.LeituraSensor;
import br.com.fiap.rotasegura.model.NivelAlerta;
import br.com.fiap.rotasegura.model.Sensor;
import br.com.fiap.rotasegura.dao.AlertaDAO;
import br.com.fiap.rotasegura.dao.LeituraSensorDAO;
import br.com.fiap.rotasegura.dao.SensorDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeituraBO {

    // A partir de 80% do limite o sistema já avisa os moradores
    private static final double PERCENTUAL_ATENCAO = 0.8;

    private final SensorDAO sensorDAO;
    private final LeituraSensorDAO leituraDAO;
    private final AlertaDAO alertaDAO;
    private final AbrigoBO abrigoBO;

    public LeituraBO(SensorDAO sensorDAO,
                     LeituraSensorDAO leituraDAO,
                     AlertaDAO alertaDAO,
                     AbrigoBO abrigoBO) {
        this.sensorDAO = sensorDAO;
        this.leituraDAO = leituraDAO;
        this.alertaDAO = alertaDAO;
        this.abrigoBO = abrigoBO;
    }

    @Transactional
    public LeituraResultadoResponse registrar(LeituraRequest request) {
        Sensor sensor = sensorDAO.findByCodigo(request.codigoSensor())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Sensor não encontrado com código " + request.codigoSensor()));

        if (!sensor.isAtivo()) {
            throw new RegraNegocioException("O sensor " + sensor.getCodigo() + " está inativo e não pode enviar leituras");
        }

        // 1. Salva a leitura
        LeituraSensor leitura = new LeituraSensor();
        leitura.setSensor(sensor);
        leitura.setNivelCm(request.nivelCm());
        leitura.setChuvaMm(request.chuvaMm());
        leitura.setDataHora(LocalDateTime.now());
        leitura = leituraDAO.save(leitura);

        // 2. Verifica se precisa gerar alerta
        NivelAlerta nivel = calcularNivel(leitura.getNivelCm(), sensor.getLimiteCm());

        if (nivel == null) {
            return new LeituraResultadoResponse(LeituraResponse.from(leitura), null, List.of());
        }

        // 3. Gera o alerta e busca abrigos com vaga no bairro do sensor
        Alerta alerta = new Alerta();
        alerta.setSensor(sensor);
        alerta.setLeitura(leitura);
        alerta.setNivel(nivel);
        alerta.setMensagem(montarMensagem(nivel, sensor, leitura));
        alerta.setBairro(sensor.getBairro());
        alerta.setDataHora(LocalDateTime.now());
        alerta.setAtivo(true);
        alerta = alertaDAO.save(alerta);

        List<AbrigoResponse> abrigos = abrigoBO.listarDisponiveisPorBairro(sensor.getBairro());

        return new LeituraResultadoResponse(LeituraResponse.from(leitura), AlertaResponse.from(alerta), abrigos);
    }

    // PERIGO: nível >= limite | ATENCAO: nível >= 80% do limite | null: sem alerta
    private NivelAlerta calcularNivel(double nivelCm, double limiteCm) {
        if (nivelCm >= limiteCm) {
            return NivelAlerta.PERIGO;
        }
        if (nivelCm >= limiteCm * PERCENTUAL_ATENCAO) {
            return NivelAlerta.ATENCAO;
        }
        return null;
    }

    private String montarMensagem(NivelAlerta nivel, Sensor sensor, LeituraSensor leitura) {
        if (nivel == NivelAlerta.PERIGO) {
            return "PERIGO: o córrego em " + sensor.getLocalizacao() + " atingiu " + leitura.getNivelCm()
                    + " cm (limite " + sensor.getLimiteCm() + " cm). Procure um abrigo no bairro " + sensor.getBairro() + ".";
        }
        return "ATENÇÃO: o córrego em " + sensor.getLocalizacao() + " está com " + leitura.getNivelCm()
                + " cm, perto do limite de " + sensor.getLimiteCm() + " cm. Fique atento e evite áreas alagáveis.";
    }
}
