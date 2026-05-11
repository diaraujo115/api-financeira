package br.com.diaraujo.financeiro.service;

import br.com.diaraujo.financeiro.dto.AuthResponse;
import br.com.diaraujo.financeiro.dto.LoginRequest;
import br.com.diaraujo.financeiro.dto.RegisterRequest;
import br.com.diaraujo.financeiro.entity.Conta;
import br.com.diaraujo.financeiro.entity.Usuario;
import br.com.diaraujo.financeiro.enums.Role;
import br.com.diaraujo.financeiro.enums.TipoConta;
import br.com.diaraujo.financeiro.repository.ContaRepository;
import br.com.diaraujo.financeiro.repository.UsuarioRepository;
import br.com.diaraujo.financeiro.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(Role.ROLE_USER)
                .build();

        usuarioRepository.save(usuario);

        Conta conta = Conta.builder()
                .usuario(usuario)
                .numeroConta(gerarNumeroConta())
                .tipo(TipoConta.CORRENTE)
                .build();

        contaRepository.save(conta);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtService.gerarToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.gerarToken(userDetails);

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        return AuthResponse.builder()
                .token(token)
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .build();
    }

    private String gerarNumeroConta() {
        String numero;
        do {
            numero = String.format("%08d", new Random().nextInt(100000000));
        } while (contaRepository.existsByNumeroConta(numero));
        return numero;
    }
}