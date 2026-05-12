package br.com.diaraujo.financeiro.controller;

import br.com.diaraujo.financeiro.dto.ContaResponse;
import br.com.diaraujo.financeiro.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @GetMapping("/minha")
    public ResponseEntity<ContaResponse> getMinhaConta() {
        return ResponseEntity.ok(contaService.getMinhaConta());
    }
}