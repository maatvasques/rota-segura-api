package br.com.fiap.rotasegura.controller;

import br.com.fiap.rotasegura.dto.AlertaRequest;
import br.com.fiap.rotasegura.dto.AlertaResponse;
import br.com.fiap.rotasegura.bo.AlertaBO;
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
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaBO alertaBO;

    public AlertaController(AlertaBO alertaBO) {
        this.alertaBO = alertaBO;
    }

    @GetMapping
    public ResponseEntity<List<AlertaResponse>> listarTodos() {
        return ResponseEntity.ok(alertaBO.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<AlertaResponse>> listarAtivos() {
        return ResponseEntity.ok(alertaBO.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alertaBO.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlertaResponse> criar(@RequestBody @Valid AlertaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alertaBO.criar(request));
    }

    @PutMapping("/{id}/encerrar")
    public ResponseEntity<AlertaResponse> encerrar(@PathVariable Long id) {
        return ResponseEntity.ok(alertaBO.encerrar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        alertaBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
