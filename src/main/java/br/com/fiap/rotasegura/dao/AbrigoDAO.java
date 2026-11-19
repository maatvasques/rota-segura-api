package br.com.fiap.rotasegura.dao;

import br.com.fiap.rotasegura.model.Abrigo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbrigoDAO extends JpaRepository<Abrigo, Long> {

    // Abrigos ativos, do bairro informado e com pelo menos uma vaga
    List<Abrigo> findByBairroIgnoreCaseAndAtivoTrueAndVagasDisponiveisGreaterThan(String bairro, int vagas);
}
