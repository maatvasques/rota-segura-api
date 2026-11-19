package br.com.fiap.rotasegura.controller;

import br.com.fiap.rotasegura.dto.AbrigoRequest;
import br.com.fiap.rotasegura.dto.AbrigoResponse;
import br.com.fiap.rotasegura.bo.AbrigoBO;
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
@RequestMapping("/abrigos")
public class AbrigoController {

    private final AbrigoBO abrigoBO;

    public AbrigoController(AbrigoBO abrigoBO) {
        this.abrigoBO = abrigoBO;
    }

    @GetMapping
    public ResponseEntity<List<AbrigoResponse>> listarTodos() {
        return ResponseEntity.ok(abrigoBO.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbrigoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(abrigoBO.buscarPorId(id));
    }

    @GetMapping("/bairro/{bairro}")
    public ResponseEntity<List<AbrigoResponse>> listarDisponiveisPorBairro(@PathVariable String bairro) {
        return ResponseEntity.ok(abrigoBO.listarDisponiveisPorBairro(bairro));
    }

    @PostMapping
    public ResponseEntity<AbrigoResponse> criar(@RequestBody @Valid AbrigoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(abrigoBO.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbrigoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid AbrigoRequest request) {
        return ResponseEntity.ok(abrigoBO.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        abrigoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
