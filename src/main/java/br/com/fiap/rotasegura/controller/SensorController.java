package br.com.fiap.rotasegura.controller;

import br.com.fiap.rotasegura.dto.LeituraResponse;
import br.com.fiap.rotasegura.dto.SensorRequest;
import br.com.fiap.rotasegura.dto.SensorResponse;
import br.com.fiap.rotasegura.bo.SensorBO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sensores")
public class SensorController {

    private final SensorBO sensorBO;

    public SensorController(SensorBO sensorBO) {
        this.sensorBO = sensorBO;
    }

    @GetMapping
    public ResponseEntity<List<SensorResponse>> listarTodos() {
        return ResponseEntity.ok(sensorBO.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sensorBO.buscarPorId(id));
    }

    @GetMapping("/{id}/leituras")
    public ResponseEntity<List<LeituraResponse>> listarLeituras(@PathVariable Long id) {
        return ResponseEntity.ok(sensorBO.listarLeituras(id));
    }

    @PostMapping
    public ResponseEntity<SensorResponse> criar(@RequestBody @Valid SensorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sensorBO.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorResponse> atualizar(@PathVariable Long id, @RequestBody @Valid SensorRequest request) {
        return ResponseEntity.ok(sensorBO.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        sensorBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
