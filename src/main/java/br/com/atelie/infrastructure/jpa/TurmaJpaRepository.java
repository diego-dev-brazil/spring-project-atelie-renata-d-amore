package br.com.atelie.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaJpaRepository extends JpaRepository<TurmaEntity, Long> {

    long countByAtivaTrue();
}
