package br.com.atelie.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialJpaRepository extends JpaRepository<MaterialEntity, Long> {

    @Query("select coalesce(sum(m.quantidade), 0) from MaterialEntity m")
    long somarQuantidade();

    @Query("select m from MaterialEntity m where m.quantidade <= m.quantidadeMinima")
    List<MaterialEntity> buscarComEstoqueBaixo();
}
