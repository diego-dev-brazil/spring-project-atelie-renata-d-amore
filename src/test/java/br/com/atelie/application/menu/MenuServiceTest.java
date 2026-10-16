package br.com.atelie.application.menu;

import br.com.atelie.domain.model.ItemMenu;
import br.com.atelie.domain.model.Perfil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MenuServiceTest {

    private final MenuService service = new MenuService();

    @Test
    void adminVeTodosOsItens() {
        List<String> rotulos = service.itensPara(Perfil.ADMIN).stream().map(ItemMenu::rotulo).toList();

        assertEquals(10, rotulos.size());
        assertTrue(rotulos.contains("Relatórios"));
        assertTrue(rotulos.contains("Usuários"));
    }

    @Test
    void funcionariaNaoVeItensDeAdmin() {
        List<String> rotulos = service.itensPara(Perfil.FUNCIONARIO).stream().map(ItemMenu::rotulo).toList();

        assertEquals(7, rotulos.size());
        assertFalse(rotulos.contains("Relatórios"));
        assertFalse(rotulos.contains("Usuários"));
        assertFalse(rotulos.contains("Configurações"));
    }

    @Test
    void encontraModuloPeloSlug() {
        assertTrue(service.buscarModulo("producao").isPresent());
        assertTrue(service.buscarModulo("inexistente").isEmpty());
    }
}

