package br.com.atelie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação.
 *
 * O Spring Boot varre o pacote {@code br.com.atelie}, encontra as classes
 * anotadas (@Service, @Repository, @Controller, @Component) e monta o grafo
 * de dependências sozinho (Injeção de Dependência). É isso que torna o
 * Princípio da Inversão de Dependência (DIP) prático no projeto.
 */
@SpringBootApplication
public class AtelieApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtelieApplication.class, args);
    }
}

