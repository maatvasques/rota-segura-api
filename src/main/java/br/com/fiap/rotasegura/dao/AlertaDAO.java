package br.com.fiap.rotasegura.dao;

import br.com.fiap.rotasegura.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaDAO extends JpaRepository<Alerta, Long> {

    List<Alerta> findAllByOrderByDataHoraDesc();

    List<Alerta> findByAtivoTrueOrderByDataHoraDesc();

    boolean existsBySensorId(Long sensorId);
}
