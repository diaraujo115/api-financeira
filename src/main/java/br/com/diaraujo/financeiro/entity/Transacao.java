package br.com.diaraujo.financeiro.entity;

import jakarta.persistence.*;
import lombok.*;
import br.com.diaraujo.financeiro.enums.TipoTransacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id")
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    private String descricao;

    @Column(name = "realizado_em", nullable = false, updatable = false)
    private LocalDateTime realizadoEm;

    @PrePersist
    public void prePersist() {
        this.realizadoEm = LocalDateTime.now();
    }
}