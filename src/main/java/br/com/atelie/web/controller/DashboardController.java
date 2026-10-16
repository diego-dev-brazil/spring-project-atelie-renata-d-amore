package br.com.atelie.web.controller;

import br.com.atelie.application.dashboard.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Painel inicial (no React: app/dashboard/page.tsx).
 *
 * Repare como ficou pequeno: no React o arquivo tinha 430 linhas misturando
 * dados, regras e HTML. Aqui o controller só pede o painel pronto ao serviço
 * e entrega para o template.
 */
@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String painel(Model model) {
        model.addAttribute("painel", dashboardService.montarPainel());
        return "dashboard";
    }
}

