package br.com.diaraujo.financeiro.repository;

import br.com.diaraujo.financeiro.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContaRepository extends JpaRepository<Conta, UUID> {
    Optional<Conta> findByUsuarioId(UUID usuarioId);
    boolean existsByNumeroConta(String numeroConta);
}