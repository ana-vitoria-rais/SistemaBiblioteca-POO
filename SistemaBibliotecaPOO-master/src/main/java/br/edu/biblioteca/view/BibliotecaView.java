package br.edu.biblioteca.view;

import br.edu.biblioteca.model.Emprestimo;
import br.edu.biblioteca.model.Livro;
import br.edu.biblioteca.model.Usuario;

import java.util.List;
import java.util.Scanner;

public class BibliotecaView {

    private final Scanner scanner = new Scanner(System.in);

    public String lerNome() {
        System.out.print("Digite o nome: ");
        return scanner.nextLine();
    }

    public String lerMatricula() {
        System.out.print("Digite a matrícula: ");
        return scanner.nextLine();
    }

    public String lerTituloLivro() {
        System.out.print("Digite o título do livro: ");
        return scanner.nextLine();
    }

    public String lerAutorLivro() {
        System.out.print("Digite o autor do livro: ");
        return scanner.nextLine();
    }

    public int lerIdLivro() {
        System.out.print("Digite o ID do livro: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    public int lerIdUsuario() {
        System.out.print("Digite o ID do usuário: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    public int lerIdEmprestimo() {
        System.out.print("Digite o ID do empréstimo: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    public int lerOpcaoMenu() {
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();
        return opcao;
    }

    public void exibirBoasVindas() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║     BIBLIOTECA UNIVERSITÁRIA     ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.println();
    }

    public void exibirMenuPrincipal() {
        System.out.println();
        System.out.println("=== MENU PRINCIPAL ===");
        System.out.println("--- LIVROS ---");
        System.out.println("1 - Listar todos os livros");
        System.out.println("2 - Cadastrar novo livro");
        System.out.println("3 - Editar livro");
        System.out.println("4 - Remover livro");
        System.out.println("--- USUÁRIOS ---");
        System.out.println("5 - Listar todos os usuários");
        System.out.println("6 - Editar usuário");
        System.out.println("7 - Remover usuário");
        System.out.println("--- EMPRÉSTIMOS ---");
        System.out.println("8 - Menu de Empréstimos");
        System.out.println("9 - Sair");
    }

    public void exibirMenuEmprestimos() {
        System.out.println();
        System.out.println("=== MENU DE EMPRÉSTIMOS ===");
        System.out.println("1 - Realizar empréstimo");
        System.out.println("2 - Realizar devolução");
        System.out.println("3 - Consultar meu histórico");
        System.out.println("4 - Listar todos os empréstimos");
        System.out.println("5 - Pagar multa");
    }

    public void exibirLivros(List<Livro> livros) {
        System.out.println();
        System.out.println("=== LIVROS ===");
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        for (int i = 0; i < livros.size(); i++) {
            Livro l = livros.get(i);
            System.out.println((i + 1) + " - " + l.getTitulo() + " - " + l.getAutor()
                    + (l.ehDisponivel() ? " [Disponível]" : " [Indisponível]"));
        }
    }

    public void exibirUsuarios(List<Usuario> usuarios) {
        System.out.println();
        System.out.println("=== USUÁRIOS CADASTRADOS ===");
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        for (Usuario u : usuarios) {
            System.out.println(u);
        }
    }

    public void exibirEmprestimos(List<Emprestimo> emprestimos) {
        System.out.println();
        System.out.println("=== EMPRÉSTIMOS ===");
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo encontrado.");
            return;
        }
        for (Emprestimo e : emprestimos) {
            System.out.println(e);
        }
    }

    public void exibirResultadoEmprestimo(Emprestimo emp) {
        System.out.println();
        System.out.println(">>> EMPRÉSTIMO REALIZADO COM SUCESSO!");
        System.out.println("Livro: " + emp.getLivro().getTitulo());
        System.out.println("Usuário: " + emp.getUsuario().getNome());
        System.out.println("Data do empréstimo: " + emp.getDataEmprestimo());
        System.out.println("Devolução prevista: " + emp.getDataDevolucaoPrevista());
    }

    public void exibirResultadoDevolucao(Emprestimo emp) {
        System.out.println();
        System.out.println(">>> DEVOLUÇÃO REGISTRADA!");
        System.out.println("Livro: " + emp.getLivro().getTitulo());
        System.out.println("Data real da devolução: " + emp.getDataRealDevolucao());

        if (emp.getDiasAtraso() > 0) {
            System.out.println("Atraso: " + emp.getDiasAtraso() + " dia(s)");
            System.out.println("Multa gerada: R$ " + emp.getMulta());
        } else {
            System.out.println("Devolvido no prazo. Sem multa!");
        }
    }

    public void exibirHistorico(List<Emprestimo> historico, String nomeUsuario) {
        System.out.println();
        System.out.println("=== HISTÓRICO DE " + nomeUsuario.toUpperCase() + " ===");
        if (historico.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
            return;
        }
        for (Emprestimo e : historico) {
            System.out.println(e);
        }
    }

    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }

    public void exibirErro(String msg) {
        System.out.println("[ERRO] " + msg);
    }

    public void fechar() {
        scanner.close();
    }
}
