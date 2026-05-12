package br.com.diaraujo.financeiro.dto;

import br.com.diaraujo.financeiro.enums.TipoTransacao;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransacaoResponse {
    private UUID id;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private String descricao;
    private String contaOrigem;
    private String contaDestino;
    private LocalDateTime realizadoEm;
}