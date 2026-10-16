package br.com.atelie.application.menu;

import br.com.atelie.domain.model.ItemMenu;
import br.com.atelie.domain.model.Perfil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Monta o menu lateral de acordo com o perfil do usuário.
 *
 * SRP: no React, a lista do menu e o filtro por perfil ficavam DENTRO do
 *      componente da tela. Agora é uma classe só para isso.
 * OCP: adicionar um item novo = adicionar uma linha na lista. O filtro
 *      ({@link ItemMenu#permitidoPara}) não muda.
 */
@Service
public class MenuService {

    private final List<ItemMenu> itens = List.of(
            ItemMenu.paraTodos("Dashboard", "/dashboard", "layout-dashboard"),
            ItemMenu.paraTodos("Notificações", "/notificacoes", "bell"),
            ItemMenu.paraTodos("Estoque e Materiais", "/estoque", "package"),
            ItemMenu.paraTodos("Modelos de Bolsas", "/modelos", "shopping-bag"),
            ItemMenu.paraTodos("Produção", "/producao", "factory"),
            ItemMenu.paraTodos("Vendas", "/vendas", "shopping-cart"),
            ItemMenu.paraTodos("Turmas", "/turmas", "graduation-cap"),
            ItemMenu.paraTodos("Alunos", "/alunos", "users"),
            ItemMenu.somenteAdmin("Relatórios", "/relatorios", "clipboard-list"),
            ItemMenu.somenteAdmin("Usuários", "/usuarios", "user-cog"),
            ItemMenu.somenteAdmin("Configurações", "/configuracoes", "settings")
    );

    public List<ItemMenu> itensPara(Perfil perfil) {
        return itens.stream()
                .filter(item -> item.permitidoPara(perfil))
                .toList();
    }

    public Optional<ItemMenu> buscarModulo(String slug) {
        String caminhoDireto = "/" + slug;
        String caminhoLegado = "/modulos/" + slug;
        return itens.stream()
                .filter(item -> item.caminho().equals(caminhoDireto) || item.caminho().equals(caminhoLegado))
                .findFirst();
    }
}
