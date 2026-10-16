package br.com.atelie.application.notificacoes;

import br.com.atelie.domain.model.Notificacao;
import br.com.atelie.domain.model.Notificacao.Categoria;
import br.com.atelie.domain.model.Usuario;
import br.com.atelie.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Gera notificações sobre aniversários de funcionários do ateliê
 * (aniversariantes do dia e aniversariantes do mês atual) (SRP + OCP + DIP).
 */
@Component
public class AniversarioFuncionarioNotificacaoProvedor implements ProvedorNotificacao {

    private static final DateTimeFormatter FORMATADOR_DIA_MES =
            DateTimeFormatter.ofPattern("dd 'de' MMMM", new Locale("pt", "BR"));

    private final UsuarioRepository usuarioRepository;

    public AniversarioFuncionarioNotificacaoProvedor(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public int ordem() {
        return 3;
    }

    @Override
    public List<Notificacao> gerarNotificacoes() {
        List<Notificacao> lista = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        for (Usuario u : usuarioRepository.listarTodos()) {
            if (u.getDataNascimento() == null) {
                continue;
            }
            if (u.aniversarioHoje(hoje)) {
                int idade = hoje.getYear() - u.getDataNascimento().getYear();
                String titulo = "Hoje é aniversário de " + u.getNome() + "!";
                String mensagem = "Nossa(o) " + u.getCargo() + " completa " + idade
                        + " anos hoje (" + u.getDataNascimento().format(FORMATADOR_DIA_MES)
                        + "). Não esqueça de parabenizar!";
                String detalhe = "Equipe do Ateliê • Perfil: " + u.getPerfil().getDescricao();

                lista.add(new Notificacao(
                        Categoria.ANIVERSARIO_FUNCIONARIO,
                        titulo,
                        mensagem,
                        detalhe,
                        "/usuarios",
                        "Ver Equipe",
                        true
                ));
            } else if (u.aniversarioNesteMes(hoje)) {
                LocalDate dataNesteAno = u.getDataNascimento().withYear(hoje.getYear());
                boolean jaPassou = dataNesteAno.isBefore(hoje);
                String titulo = "Aniversariante do Mês: " + u.getNome();
                String mensagem = jaPassou
                        ? u.getNome() + " (" + u.getCargo() + ") fez aniversário no dia "
                          + u.getDataNascimento().format(FORMATADOR_DIA_MES) + "."
                        : u.getNome() + " (" + u.getCargo() + ") fará aniversário no dia "
                          + u.getDataNascimento().format(FORMATADOR_DIA_MES) + ".";
                String detalhe = "Aniversariantes de " + hoje.format(DateTimeFormatter.ofPattern("MMMM", new Locale("pt", "BR")));

                lista.add(new Notificacao(
                        Categoria.ANIVERSARIO_FUNCIONARIO,
                        titulo,
                        mensagem,
                        detalhe,
                        "/usuarios",
                        "Ver Equipe",
                        false
                ));
            }
        }

        return lista;
    }
}

