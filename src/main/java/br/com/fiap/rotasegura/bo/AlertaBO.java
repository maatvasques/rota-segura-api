package br.com.fiap.rotasegura.bo;

import br.com.fiap.rotasegura.dto.AlertaRequest;
import br.com.fiap.rotasegura.dto.AlertaResponse;
import br.com.fiap.rotasegura.exception.RecursoNaoEncontradoException;
import br.com.fiap.rotasegura.exception.RegraNegocioException;
import br.com.fiap.rotasegura.model.Alerta;
import br.com.fiap.rotasegura.model.Sensor;
import br.com.fiap.rotasegura.dao.AlertaDAO;
import br.com.fiap.rotasegura.dao.SensorDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaBO {

    private final AlertaDAO alertaDAO;
    private final SensorDAO sensorDAO;

    public AlertaBO(AlertaDAO alertaDAO, SensorDAO sensorDAO) {
        this.alertaDAO = alertaDAO;
        this.sensorDAO = sensorDAO;
    }

    public List<AlertaResponse> listarTodos() {
        return alertaDAO.findAllByOrderByDataHoraDesc().stream()
                .map(AlertaResponse::from)
                .toList();
    }

    public List<AlertaResponse> listarAtivos() {
        return alertaDAO.findByAtivoTrueOrderByDataHoraDesc().stream()
                .map(AlertaResponse::from)
                .toList();
    }

    public AlertaResponse buscarPorId(Long id) {
        return AlertaResponse.from(buscarEntidade(id));
    }

    // Alerta manual (ex: agente da Defesa Civil)
    public AlertaResponse criar(AlertaRequest request) {
        Alerta alerta = new Alerta();

        if (request.sensorId() != null) {
            Sensor sensor = sensorDAO.findById(request.sensorId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Sensor não encontrado com id " + request.sensorId()));
            alerta.setSensor(sensor);
        }

        alerta.setNivel(request.nivel());
        alerta.setMensagem(request.mensagem());
        alerta.setBairro(request.bairro());
        alerta.setDataHora(LocalDateTime.now());
        alerta.setAtivo(true);

        return AlertaResponse.from(alertaDAO.save(alerta));
    }

    public AlertaResponse encerrar(Long id) {
        Alerta alerta = buscarEntidade(id);

        if (!alerta.isAtivo()) {
            throw new RegraNegocioException("O alerta " + id + " já está encerrado");
        }

        alerta.setAtivo(false);
        return AlertaResponse.from(alertaDAO.save(alerta));
    }

    public void excluir(Long id) {
        Alerta alerta = buscarEntidade(id);
        alertaDAO.delete(alerta);
    }

    private Alerta buscarEntidade(Long id) {
        return alertaDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Alerta não encontrado com id " + id));
    }
}
