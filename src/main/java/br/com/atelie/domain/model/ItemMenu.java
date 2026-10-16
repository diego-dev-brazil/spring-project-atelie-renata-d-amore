package br.com.atelie.domain.model;

import java.util.EnumSet;
import java.util.Set;

/**
 * Item do menu lateral.
 *
 * No React existia o campo {@code adminOnly?: boolean}. Aqui o item diz
 * explicitamente QUAIS perfis podem vê-lo. Se amanhã surgir um perfil novo
 * (ex.: PROFESSORA), não precisamos mudar a lógica de filtro — só os dados (OCP).
 */
public record ItemMenu(String rotulo, String caminho, String icone, Set<Perfil> perfisPermitidos) {

    public static ItemMenu paraTodos(String rotulo, String caminho, String icone) {
        return new ItemMenu(rotulo, caminho, icone, EnumSet.allOf(Perfil.class));
    }

    public static ItemMenu somenteAdmin(String rotulo, String caminho, String icone) {
        return new ItemMenu(rotulo, caminho, icone, EnumSet.of(Perfil.ADMIN));
    }

    public boolean permitidoPara(Perfil perfil) {
        return perfisPermitidos.contains(perfil);
    }
}

