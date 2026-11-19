package br.com.fiap.rotasegura.dao;

import br.com.fiap.rotasegura.model.LeituraSensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeituraSensorDAO extends JpaRepository<LeituraSensor, Long> {

    // Histórico do sensor, da leitura mais recente para a mais antiga
    List<LeituraSensor> findBySensorIdOrderByDataHoraDesc(Long sensorId);

    boolean existsBySensorId(Long sensorId);
}
