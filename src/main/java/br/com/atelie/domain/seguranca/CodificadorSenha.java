package br.com.atelie.domain.seguranca;

/**
 * Sabe transformar uma senha em hash e comparar senhas.
 *
 * DIP + OCP: o serviço de autenticação usa esta abstração. Hoje a
 * implementação é SHA-256; se o professor pedir BCrypt, criamos outra classe
 * que implementa esta interface e nada mais muda.
 */
public interface CodificadorSenha {

    String codificar(String senhaPura);

    boolean confere(String senhaPura, String senhaCodificada);
}

