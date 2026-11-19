package br.com.fiap.rotasegura.bo;

import br.com.fiap.rotasegura.dto.AbrigoRequest;
import br.com.fiap.rotasegura.dto.AbrigoResponse;
import br.com.fiap.rotasegura.exception.RecursoNaoEncontradoException;
import br.com.fiap.rotasegura.exception.RegraNegocioException;
import br.com.fiap.rotasegura.model.Abrigo;
import br.com.fiap.rotasegura.dao.AbrigoDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoBO {

    private final AbrigoDAO abrigoDAO;

    public AbrigoBO(AbrigoDAO abrigoDAO) {
        this.abrigoDAO = abrigoDAO;
    }

    public List<AbrigoResponse> listarTodos() {
        return abrigoDAO.findAll().stream()
                .map(AbrigoResponse::from)
                .toList();
    }

    public AbrigoResponse buscarPorId(Long id) {
        return AbrigoResponse.from(buscarEntidade(id));
    }

    // Abrigos ativos e com vaga no bairro
    public List<AbrigoResponse> listarDisponiveisPorBairro(String bairro) {
        return abrigoDAO.findByBairroIgnoreCaseAndAtivoTrueAndVagasDisponiveisGreaterThan(bairro, 0).stream()
                .map(AbrigoResponse::from)
                .toList();
    }

    public AbrigoResponse criar(AbrigoRequest request) {
        Abrigo abrigo = new Abrigo();
        preencherDados(abrigo, request);
        return AbrigoResponse.from(abrigoDAO.save(abrigo));
    }

    public AbrigoResponse atualizar(Long id, AbrigoRequest request) {
        Abrigo abrigo = buscarEntidade(id);
        preencherDados(abrigo, request);
        return AbrigoResponse.from(abrigoDAO.save(abrigo));
    }

    public void excluir(Long id) {
        Abrigo abrigo = buscarEntidade(id);
        abrigoDAO.delete(abrigo);
    }

    private Abrigo buscarEntidade(Long id) {
        return abrigoDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Abrigo não encontrado com id " + id));
    }

    private void preencherDados(Abrigo abrigo, AbrigoRequest request) {
        if (request.vagasDisponiveis() > request.capacidade()) {
            throw new RegraNegocioException("As vagas disponíveis não podem ser maiores que a capacidade do abrigo");
        }

        abrigo.setNome(request.nome());
        abrigo.setEndereco(request.endereco());
        abrigo.setBairro(request.bairro());
        abrigo.setCapacidade(request.capacidade());
        abrigo.setVagasDisponiveis(request.vagasDisponiveis());
        abrigo.setLatitude(request.latitude());
        abrigo.setLongitude(request.longitude());
        abrigo.setAtivo(request.ativo() == null || request.ativo());
    }
}
