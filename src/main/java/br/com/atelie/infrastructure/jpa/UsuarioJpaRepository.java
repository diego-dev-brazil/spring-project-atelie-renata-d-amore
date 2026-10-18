package br.com.atelie.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, String> {
}
