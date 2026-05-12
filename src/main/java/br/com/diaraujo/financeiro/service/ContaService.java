package br.com.diaraujo.financeiro.service;

import br.com.diaraujo.financeiro.dto.ContaResponse;
import br.com.diaraujo.financeiro.entity.Conta;
import br.com.diaraujo.financeiro.entity.Usuario;
import br.com.diaraujo.financeiro.repository.ContaRepository;
import br.com.diaraujo.financeiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    public ContaResponse getMinhaConta() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Conta conta = contaRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        return ContaResponse.builder()
                .id(conta.getId())
                .numeroConta(conta.getNumeroConta())
                .saldo(conta.getSaldo())
                .tipo(conta.getTipo())
                .ativa(conta.getAtiva())
                .nomeUsuario(usuario.getNome())
                .build();
    }
}