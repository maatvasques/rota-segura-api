package br.com.fiap.rotasegura.controller;

import br.com.fiap.rotasegura.dto.LeituraRequest;
import br.com.fiap.rotasegura.dto.LeituraResultadoResponse;
import br.com.fiap.rotasegura.bo.LeituraBO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leituras")
public class LeituraController {

    private final LeituraBO leituraBO;

    public LeituraController(LeituraBO leituraBO) {
        this.leituraBO = leituraBO;
    }

    // Recebe a leitura do sensor e, se precisar, gera o alerta
    @PostMapping
    public ResponseEntity<LeituraResultadoResponse> registrar(@RequestBody @Valid LeituraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leituraBO.registrar(request));
    }
}
