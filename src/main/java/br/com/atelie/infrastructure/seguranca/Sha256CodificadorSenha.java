package br.com.atelie.infrastructure.seguranca;

import br.com.atelie.domain.seguranca.CodificadorSenha;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Implementação de {@link CodificadorSenha} usando SHA-256.
 *
 * Observação: em produção o ideal é BCrypt/Argon2 (com "salt"). Como o
 * resto do sistema depende só da interface, trocar é criar outra classe.
 */
@Component
public class Sha256CodificadorSenha implements CodificadorSenha {

    @Override
    public String codificar(String senhaPura) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senhaPura.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 não disponível na JVM", e);
        }
    }

    @Override
    public boolean confere(String senhaPura, String senhaCodificada) {
        byte[] esperado = senhaCodificada.getBytes(StandardCharsets.UTF_8);
        byte[] recebido = codificar(senhaPura).getBytes(StandardCharsets.UTF_8);
        // Comparação em tempo constante para evitar ataques de temporização
        return MessageDigest.isEqual(esperado, recebido);
    }
}

