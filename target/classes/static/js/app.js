// Comportamentos simples de interface (o que no React era feito com useState).

document.addEventListener("DOMContentLoaded", () => {
  // Desenha os ícones Lucide (<i data-lucide="...">)
  if (window.lucide) {
    window.lucide.createIcons();
  }

  // Abrir/fechar o menu lateral no celular
  const sidebar = document.getElementById("sidebar");
  const overlay = document.getElementById("sidebar-overlay");

  const abrirMenu = () => {
    sidebar?.classList.remove("-translate-x-full");
    overlay?.classList.remove("hidden");
  };
  const fecharMenu = () => {
    sidebar?.classList.add("-translate-x-full");
    overlay?.classList.add("hidden");
  };

  document.querySelectorAll("[data-abrir-menu]").forEach((el) => el.addEventListener("click", abrirMenu));
  document.querySelectorAll("[data-fechar-menu]").forEach((el) => el.addEventListener("click", fecharMenu));

  // Botão "Entrando..." no login (no React era o estado isLoading)
  const formLogin = document.getElementById("form-login");
  const botaoEntrar = document.getElementById("botao-entrar");
  formLogin?.addEventListener("submit", () => {
    botaoEntrar.disabled = true;
    botaoEntrar.textContent = "Entrando...";
  });

  // Abrir/fechar o dropdown de notificações do sino no topo
  const botaoNotificacoes = document.querySelector("[data-toggle-notificacoes]");
  const painelNotificacoes = document.getElementById("painel-notificacoes");
  const containerNotificacoes = document.getElementById("container-notificacoes");

  botaoNotificacoes?.addEventListener("click", (e) => {
    e.stopPropagation();
    painelNotificacoes?.classList.toggle("hidden");
  });

  document.addEventListener("click", (e) => {
    if (painelNotificacoes && !painelNotificacoes.classList.contains("hidden")) {
      if (!containerNotificacoes?.contains(e.target)) {
        painelNotificacoes.classList.add("hidden");
      }
    }
  });
});

