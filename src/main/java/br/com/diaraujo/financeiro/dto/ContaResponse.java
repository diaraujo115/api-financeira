package br.com.diaraujo.financeiro.dto;

import br.com.diaraujo.financeiro.enums.TipoConta;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ContaResponse {
    private UUID id;
    private String numeroConta;
    private BigDecimal saldo;
    private TipoConta tipo;
    private Boolean ativa;
    private String nomeUsuario;
}