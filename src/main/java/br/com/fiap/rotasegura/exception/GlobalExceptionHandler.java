package br.com.fiap.rotasegura.exception;

import br.com.fiap.rotasegura.dto.ErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

// Centraliza o tratamento de erros: toda resposta de erro sai no formato ErroResponse
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 - recurso não encontrado
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException ex) {
        return montarErro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 404 - rota que não existe (ex: /abrigoss)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> rotaNaoEncontrada(NoResourceFoundException ex) {
        return montarErro(HttpStatus.NOT_FOUND, "Rota não encontrada: /" + ex.getResourcePath());
    }

    // 400 - campos inválidos (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return montarErro(HttpStatus.BAD_REQUEST, mensagem);
    }

    // 400 - JSON mal formatado ou valor inválido (ex: nivel "MEDIO")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> jsonInvalido(HttpMessageNotReadableException ex) {
        return montarErro(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido. Verifique o JSON enviado.");
    }

    // 400 - parâmetro da URL com tipo errado (ex: /abrigos/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return montarErro(HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue());
    }

    // 400 - regra de negócio
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regraNegocio(RegraNegocioException ex) {
        return montarErro(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 405 - método HTTP não suportado na rota (ex: DELETE /leituras)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> metodoNaoSuportado(HttpRequestMethodNotSupportedException ex) {
        return montarErro(HttpStatus.METHOD_NOT_ALLOWED,
                "Método " + ex.getMethod() + " não é suportado nesta rota");
    }

    // 409 - violação de restrição do banco (ex: chave estrangeira, valor único)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> integridade(DataIntegrityViolationException ex) {
        return montarErro(HttpStatus.CONFLICT,
                "A operação viola uma restrição do banco de dados (registro em uso ou duplicado)");
    }

    // 500 - qualquer erro não previsto (não expõe detalhes internos ao cliente)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> erroInesperado(Exception ex) {
        log.error("Erro inesperado", ex);
        return montarErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor. Tente novamente mais tarde.");
    }

    private ResponseEntity<ErroResponse> montarErro(HttpStatus status, String mensagem) {
        ErroResponse erro = new ErroResponse(status.value(), mensagem, LocalDateTime.now());
        return ResponseEntity.status(status).body(erro);
    }
}
