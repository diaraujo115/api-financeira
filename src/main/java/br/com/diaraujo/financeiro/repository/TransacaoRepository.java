package br.com.diaraujo.financeiro.repository;

import br.com.diaraujo.financeiro.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {

    List<Transacao> findByContaOrigemIdOrContaDestinoIdOrderByRealizadoEmDesc(
            UUID contaOrigemId, UUID contaDestinoId
    );

    List<Transacao> findByContaOrigemIdOrContaDestinoIdAndRealizadoEmBetweenOrderByRealizadoEmDesc(
            UUID contaOrigemId, UUID contaDestinoId,
            LocalDateTime inicio, LocalDateTime fim
    );
}