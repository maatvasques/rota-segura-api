package br.com.fiap.rotasegura;

import br.com.fiap.rotasegura.model.Abrigo;
import br.com.fiap.rotasegura.model.LeituraSensor;
import br.com.fiap.rotasegura.model.Sensor;
import br.com.fiap.rotasegura.dao.AbrigoDAO;
import br.com.fiap.rotasegura.dao.LeituraSensorDAO;
import br.com.fiap.rotasegura.dao.SensorDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// Insere dados de exemplo ao iniciar, só se as tabelas estiverem vazias
@Component
public class DadosIniciais implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DadosIniciais.class);

    private final AbrigoDAO abrigoDAO;
    private final SensorDAO sensorDAO;
    private final LeituraSensorDAO leituraDAO;

    public DadosIniciais(AbrigoDAO abrigoDAO,
                         SensorDAO sensorDAO,
                         LeituraSensorDAO leituraDAO) {
        this.abrigoDAO = abrigoDAO;
        this.sensorDAO = sensorDAO;
        this.leituraDAO = leituraDAO;
    }

    @Override
    public void run(String... args) {
        if (abrigoDAO.count() == 0) {
            criarAbrigos();
        }
        if (sensorDAO.count() == 0) {
            criarSensoresELeituras();
        }
    }

    private void criarAbrigos() {
        abrigoDAO.save(novoAbrigo("EMEF Jardim Pantanal", "Rua das Acácias, 120",
                "Jardim Pantanal", 150, 120, -23.4952, -46.4318));
        // Abrigo lotado: não aparece na busca de abrigos com vaga
        abrigoDAO.save(novoAbrigo("Ginásio Poliesportivo Pantanal", "Av. dos Ipês, 800",
                "Jardim Pantanal", 300, 0, -23.4938, -46.4285));
        abrigoDAO.save(novoAbrigo("Salão Comunitário Jardim Helena", "Rua do Porto, 45",
                "Jardim Helena", 80, 50, -23.4870, -46.4205));
        abrigoDAO.save(novoAbrigo("Centro Esportivo Vila Itaim", "Av. Central, 1500",
                "Vila Itaim", 400, 350, -23.4975, -46.4050));
        log.info("Dados iniciais: 4 abrigos cadastrados");
    }

    private void criarSensoresELeituras() {
        Sensor sns1 = sensorDAO.save(novoSensor("SNS-001",
                "Córrego Três Pontes - ponte da Rua das Flores", "Jardim Pantanal", 180.0));
        Sensor sns2 = sensorDAO.save(novoSensor("SNS-002",
                "Córrego Itaim - ao lado da escola estadual", "Jardim Helena", 150.0));
        Sensor sns3 = sensorDAO.save(novoSensor("SNS-003",
                "Várzea do Tietê - Av. Central", "Vila Itaim", 200.0));

        // Leituras normais (abaixo de 80% do limite, não geram alerta)
        leituraDAO.save(novaLeitura(sns1, 60.0, 5.0, 6));
        leituraDAO.save(novaLeitura(sns1, 95.5, 18.2, 3));
        leituraDAO.save(novaLeitura(sns2, 40.0, 2.0, 5));
        leituraDAO.save(novaLeitura(sns2, 70.0, 12.5, 2));
        leituraDAO.save(novaLeitura(sns3, 88.0, 8.0, 1));
        log.info("Dados iniciais: 3 sensores e 5 leituras cadastrados");
    }

    private Abrigo novoAbrigo(String nome, String endereco, String bairro, int capacidade,
                              int vagas, double latitude, double longitude) {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome(nome);
        abrigo.setEndereco(endereco);
        abrigo.setBairro(bairro);
        abrigo.setCapacidade(capacidade);
        abrigo.setVagasDisponiveis(vagas);
        abrigo.setLatitude(latitude);
        abrigo.setLongitude(longitude);
        abrigo.setAtivo(true);
        return abrigo;
    }

    private Sensor novoSensor(String codigo, String localizacao, String bairro, double limiteCm) {
        Sensor sensor = new Sensor();
        sensor.setCodigo(codigo);
        sensor.setLocalizacao(localizacao);
        sensor.setBairro(bairro);
        sensor.setLimiteCm(limiteCm);
        sensor.setAtivo(true);
        return sensor;
    }

    private LeituraSensor novaLeitura(Sensor sensor, double nivelCm, double chuvaMm, int horasAtras) {
        LeituraSensor leitura = new LeituraSensor();
        leitura.setSensor(sensor);
        leitura.setNivelCm(nivelCm);
        leitura.setChuvaMm(chuvaMm);
        leitura.setDataHora(LocalDateTime.now().minusHours(horasAtras));
        return leitura;
    }
}
