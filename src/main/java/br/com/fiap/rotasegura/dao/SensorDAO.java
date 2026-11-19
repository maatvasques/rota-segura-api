package br.com.fiap.rotasegura.dao;

import br.com.fiap.rotasegura.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorDAO extends JpaRepository<Sensor, Long> {

    Optional<Sensor> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
