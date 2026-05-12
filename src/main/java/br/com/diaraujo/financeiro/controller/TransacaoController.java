package br.com.diaraujo.financeiro.controller;

import br.com.diaraujo.financeiro.dto.TransacaoRequest;
import br.com.diaraujo.financeiro.dto.TransacaoResponse;
import br.com.diaraujo.financeiro.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping("/deposito")
    public ResponseEntity<TransacaoResponse> depositar(@RequestBody @Valid TransacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoService.depositar(request));
    }

    @PostMapping("/saque")
    public ResponseEntity<TransacaoResponse> sacar(@RequestBody @Valid TransacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoService.sacar(request));
    }

    @PostMapping("/transferencia")
    public ResponseEntity<TransacaoResponse> transferir(@RequestBody @Valid TransacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoService.transferir(request));
    }

    @GetMapping("/extrato")
    public ResponseEntity<List<TransacaoResponse>> extrato() {
        return ResponseEntity.ok(transacaoService.extrato());
    }
}