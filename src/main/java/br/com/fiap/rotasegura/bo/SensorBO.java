package br.com.fiap.rotasegura.bo;

import br.com.fiap.rotasegura.dto.LeituraResponse;
import br.com.fiap.rotasegura.dto.SensorRequest;
import br.com.fiap.rotasegura.dto.SensorResponse;
import br.com.fiap.rotasegura.exception.RecursoNaoEncontradoException;
import br.com.fiap.rotasegura.exception.RegraNegocioException;
import br.com.fiap.rotasegura.model.Sensor;
import br.com.fiap.rotasegura.dao.AlertaDAO;
import br.com.fiap.rotasegura.dao.LeituraSensorDAO;
import br.com.fiap.rotasegura.dao.SensorDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorBO {

    private final SensorDAO sensorDAO;
    private final LeituraSensorDAO leituraDAO;
    private final AlertaDAO alertaDAO;

    public SensorBO(SensorDAO sensorDAO,
                    LeituraSensorDAO leituraDAO,
                    AlertaDAO alertaDAO) {
        this.sensorDAO = sensorDAO;
        this.leituraDAO = leituraDAO;
        this.alertaDAO = alertaDAO;
    }

    public List<SensorResponse> listarTodos() {
        return sensorDAO.findAll().stream()
                .map(SensorResponse::from)
                .toList();
    }

    public SensorResponse buscarPorId(Long id) {
        return SensorResponse.from(buscarEntidade(id));
    }

    public List<LeituraResponse> listarLeituras(Long id) {
        buscarEntidade(id); // garante 404 se o sensor não existir
        return leituraDAO.findBySensorIdOrderByDataHoraDesc(id).stream()
                .map(LeituraResponse::from)
                .toList();
    }

    public SensorResponse criar(SensorRequest request) {
        if (sensorDAO.existsByCodigo(request.codigo())) {
            throw new RegraNegocioException("Já existe um sensor com o código " + request.codigo());
        }

        Sensor sensor = new Sensor();
        preencherDados(sensor, request);
        return SensorResponse.from(sensorDAO.save(sensor));
    }

    public SensorResponse atualizar(Long id, SensorRequest request) {
        Sensor sensor = buscarEntidade(id);

        // Se trocou o código, o novo código não pode estar em uso
        boolean trocouCodigo = !sensor.getCodigo().equals(request.codigo());
        if (trocouCodigo && sensorDAO.existsByCodigo(request.codigo())) {
            throw new RegraNegocioException("Já existe um sensor com o código " + request.codigo());
        }

        preencherDados(sensor, request);
        return SensorResponse.from(sensorDAO.save(sensor));
    }

    public void excluir(Long id) {
        Sensor sensor = buscarEntidade(id);

        // Não apaga o histórico: sensor com leituras/alertas deve ser desativado
        if (leituraDAO.existsBySensorId(id) || alertaDAO.existsBySensorId(id)) {
            throw new RegraNegocioException(
                    "O sensor possui leituras ou alertas registrados. Desative-o (ativo = false) em vez de excluir.");
        }

        sensorDAO.delete(sensor);
    }

    private Sensor buscarEntidade(Long id) {
        return sensorDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sensor não encontrado com id " + id));
    }

    private void preencherDados(Sensor sensor, SensorRequest request) {
        sensor.setCodigo(request.codigo());
        sensor.setLocalizacao(request.localizacao());
        sensor.setBairro(request.bairro());
        sensor.setLimiteCm(request.limiteCm());
        sensor.setAtivo(request.ativo() == null || request.ativo());
    }
}
