package br.com.fiap.rotasegura.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_RS_LEITURA")
public class LeituraSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(nullable = false)
    private double nivelCm;

    @Column(nullable = false)
    private double chuvaMm;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public LeituraSensor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public double getNivelCm() {
        return nivelCm;
    }

    public void setNivelCm(double nivelCm) {
        this.nivelCm = nivelCm;
    }

    public double getChuvaMm() {
        return chuvaMm;
    }

    public void setChuvaMm(double chuvaMm) {
        this.chuvaMm = chuvaMm;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
