package br.com.atelie.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoJpaRepository extends JpaRepository<AlunoEntity, Long> {

    List<AlunoEntity> findByParcelasPendentesGreaterThanOrderByParcelasPendentesDesc(int minimo);
}
