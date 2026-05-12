package br.com.diaraujo.financeiro.service;

import br.com.diaraujo.financeiro.dto.TransacaoRequest;
import br.com.diaraujo.financeiro.dto.TransacaoResponse;
import br.com.diaraujo.financeiro.entity.Conta;
import br.com.diaraujo.financeiro.entity.Transacao;
import br.com.diaraujo.financeiro.entity.Usuario;
import br.com.diaraujo.financeiro.enums.TipoTransacao;
import br.com.diaraujo.financeiro.repository.ContaRepository;
import br.com.diaraujo.financeiro.repository.TransacaoRepository;
import br.com.diaraujo.financeiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public TransacaoResponse depositar(TransacaoRequest request) {
        Conta conta = getContaDoUsuarioLogado();

        conta.setSaldo(conta.getSaldo().add(request.getValor()));
        contaRepository.save(conta);

        Transacao transacao = Transacao.builder()
                .contaDestino(conta)
                .valor(request.getValor())
                .tipo(TipoTransacao.DEPOSITO)
                .descricao(request.getDescricao() != null ? request.getDescricao() : "Depósito")
                .build();

        return toResponse(transacaoRepository.save(transacao));
    }

    @Transactional
    public TransacaoResponse sacar(TransacaoRequest request) {
        Conta conta = getContaDoUsuarioLogado();

        if (conta.getSaldo().compareTo(request.getValor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo().subtract(request.getValor()));
        contaRepository.save(conta);

        Transacao transacao = Transacao.builder()
                .contaOrigem(conta)
                .valor(request.getValor())
                .tipo(TipoTransacao.SAQUE)
                .descricao(request.getDescricao() != null ? request.getDescricao() : "Saque")
                .build();

        return toResponse(transacaoRepository.save(transacao));
    }

    @Transactional
    public TransacaoResponse transferir(TransacaoRequest request) {
        if (request.getNumeroContaDestino() == null || request.getNumeroContaDestino().isBlank()) {
            throw new IllegalArgumentException("Número da conta destino é obrigatório");
        }

        Conta contaOrigem = getContaDoUsuarioLogado();

        Conta contaDestino = contaRepository.findByNumeroConta(request.getNumeroContaDestino())
                .orElseThrow(() -> new IllegalArgumentException("Conta destino não encontrada"));

        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new IllegalArgumentException("Não é possível transferir para a própria conta");
        }

        if (contaOrigem.getSaldo().compareTo(request.getValor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.getValor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.getValor()));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transacao transacao = Transacao.builder()
                .contaOrigem(contaOrigem)
                .contaDestino(contaDestino)
                .valor(request.getValor())
                .tipo(TipoTransacao.TRANSFERENCIA)
                .descricao(request.getDescricao() != null ? request.getDescricao() : "Transferência")
                .build();

        return toResponse(transacaoRepository.save(transacao));
    }

    public List<TransacaoResponse> extrato() {
        Conta conta = getContaDoUsuarioLogado();

        return transacaoRepository
                .findByContaOrigemIdOrContaDestinoIdOrderByRealizadoEmDesc(
                        conta.getId(), conta.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Conta getContaDoUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        return contaRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
    }

    private TransacaoResponse toResponse(Transacao t) {
        return TransacaoResponse.builder()
                .id(t.getId())
                .valor(t.getValor())
                .tipo(t.getTipo())
                .descricao(t.getDescricao())
                .contaOrigem(t.getContaOrigem() != null ? t.getContaOrigem().getNumeroConta() : null)
                .contaDestino(t.getContaDestino() != null ? t.getContaDestino().getNumeroConta() : null)
                .realizadoEm(t.getRealizadoEm())
                .build();
    }
}