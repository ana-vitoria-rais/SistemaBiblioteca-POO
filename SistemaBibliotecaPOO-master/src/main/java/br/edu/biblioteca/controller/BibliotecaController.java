package br.edu.biblioteca.controller;

import br.edu.biblioteca.dao.EmprestimoDAO;
import br.edu.biblioteca.dao.LivroDAO;
import br.edu.biblioteca.dao.UsuarioDAO;
import br.edu.biblioteca.dao.impl.EmprestimoDAOImpl;
import br.edu.biblioteca.dao.impl.LivroDAOImpl;
import br.edu.biblioteca.dao.impl.UsuarioDAOImpl;
import br.edu.biblioteca.excecao.LivroIndisponivelException;
import br.edu.biblioteca.excecao.LivroNaoEncontradoException;
import br.edu.biblioteca.excecao.MultaPendenteException;
import br.edu.biblioteca.model.Emprestimo;
import br.edu.biblioteca.model.Livro;
import br.edu.biblioteca.model.Usuario;
import br.edu.biblioteca.view.BibliotecaView;

import java.util.List;

public class BibliotecaController {

    private final BibliotecaView view;

    private final LivroDAO livroDAO         = new LivroDAOImpl();
    private final UsuarioDAO usuarioDAO     = new UsuarioDAOImpl();
    private final EmprestimoDAO empDAO      = new EmprestimoDAOImpl();

    private Usuario usuarioLogado;

    public BibliotecaController(BibliotecaView view) {
        this.view = view;
    }

    public void iniciar() {
        view.exibirBoasVindas();
        loginOuCadastro();

        int opcao = 0;
        while (opcao != 9) {
            view.exibirMenuPrincipal();
            opcao = view.lerOpcaoMenu();
            switch (opcao) {
                case 1 -> listarLivros();
                case 2 -> cadastrarLivro();
                case 3 -> editarLivro();
                case 4 -> removerLivro();
                case 5 -> listarUsuarios();
                case 6 -> editarUsuario();
                case 7 -> removerUsuario();
                case 8 -> menuEmprestimos();
                case 9 -> view.exibirMensagem("Encerrando o sistema. Até logo!");
                default -> view.exibirMensagem("Opção inválida.");
            }
        }
        view.fechar();
    }

    private void loginOuCadastro() {
        String matricula = view.lerMatricula();
        usuarioLogado = usuarioDAO.buscarPorMatricula(matricula);

        if (usuarioLogado == null) {
            view.exibirMensagem("Matrícula não encontrada. Vamos fazer seu cadastro!");
            String nome = view.lerNome();
            usuarioLogado = new Usuario(nome, matricula);
            usuarioDAO.salvar(usuarioLogado);
            view.exibirMensagem("Usuário cadastrado com sucesso! Bem-vindo, " + nome + "!");
        } else {
            view.exibirMensagem("Bem-vindo de volta, " + usuarioLogado.getNome() + "!");
        }
    }

    private void listarLivros() {
        List<Livro> livros = livroDAO.buscarTodos();
        view.exibirLivros(livros);
    }

    private void cadastrarLivro() {
        String titulo = view.lerTituloLivro();
        String autor  = view.lerAutorLivro();
        Livro novoLivro = new Livro(titulo, autor);
        livroDAO.salvar(novoLivro);
        view.exibirMensagem("Livro cadastrado com sucesso! ID gerado: " + novoLivro.getId());
    }

    private void editarLivro() {
        int id = view.lerIdLivro();
        Livro livro = livroDAO.buscarPorId(id);

        if (livro == null) {
            view.exibirMensagem("Livro com ID " + id + " não encontrado.");
            return;
        }

        view.exibirMensagem("Livro atual: " + livro);
        String novoTitulo = view.lerTituloLivro();
        String novoAutor  = view.lerAutorLivro();
        livro.setTitulo(novoTitulo);
        livro.setAutor(novoAutor);
        livroDAO.atualizar(livro);
        view.exibirMensagem("Livro atualizado com sucesso!");
    }

    private void removerLivro() {
        int id = view.lerIdLivro();
        Livro livro = livroDAO.buscarPorId(id);

        if (livro == null) {
            view.exibirMensagem("Livro com ID " + id + " não encontrado.");
            return;
        }

        livroDAO.deletar(id);
        view.exibirMensagem("Livro \"" + livro.getTitulo() + "\" removido com sucesso.");
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = usuarioDAO.buscarTodos();
        view.exibirUsuarios(usuarios);
    }

    private void editarUsuario() {
        int id = view.lerIdUsuario();
        Usuario usuario = usuarioDAO.buscarPorId(id);

        if (usuario == null) {
            view.exibirMensagem("Usuário com ID " + id + " não encontrado.");
            return;
        }

        view.exibirMensagem("Usuário atual: " + usuario);
        String novoNome      = view.lerNome();
        String novaMatricula = view.lerMatricula();
        usuario.setNome(novoNome);
        usuario.setMatricula(novaMatricula);
        usuarioDAO.atualizar(usuario);
        view.exibirMensagem("Usuário atualizado com sucesso!");
    }

    private void removerUsuario() {
        int id = view.lerIdUsuario();
        Usuario usuario = usuarioDAO.buscarPorId(id);

        if (usuario == null) {
            view.exibirMensagem("Usuário com ID " + id + " não encontrado.");
            return;
        }

        usuarioDAO.deletar(id);
        view.exibirMensagem("Usuário \"" + usuario.getNome() + "\" removido com sucesso.");
    }

    private void menuEmprestimos() {
        view.exibirMenuEmprestimos();
        int opcao = view.lerOpcaoMenu();
        switch (opcao) {
            case 1 -> realizarEmprestimo();
            case 2 -> realizarDevolucao();
            case 3 -> consultarHistorico();
            case 4 -> listarTodosEmprestimos();
            case 5 -> pagarMulta();
            default -> view.exibirMensagem("Opção inválida.");
        }
    }

    private void realizarEmprestimo() {
        if (usuarioLogado.temMultaPendente()) {
            try {
                throw new MultaPendenteException(
                        "Você possui multa pendente de R$ " + usuarioLogado.getMultaPendente()
                        + ". Pague antes de realizar um novo empréstimo.");
            } catch (MultaPendenteException e) {
                view.exibirErro(e.getMessage());
                return;
            }
        }

        List<Livro> disponiveis = livroDAO.buscarDisponiveis();
        if (disponiveis.isEmpty()) {
            view.exibirMensagem("Nenhum livro disponível no momento.");
            return;
        }

        view.exibirLivros(disponiveis);
        int livroId = view.lerIdLivro();
        Livro livro = livroDAO.buscarPorId(livroId);

        try {
            if (livro == null) {
                throw new LivroNaoEncontradoException("Livro com ID " + livroId + " não encontrado.");
            }
            if (!livro.ehDisponivel()) {
                throw new LivroIndisponivelException("O livro \"" + livro.getTitulo() + "\" não está disponível.");
            }

            Emprestimo emp = new Emprestimo(livro, usuarioLogado);
            empDAO.salvar(emp);
            view.exibirResultadoEmprestimo(emp);

        } catch (LivroNaoEncontradoException | LivroIndisponivelException e) {
            view.exibirErro(e.getMessage());
        }
    }

    private void realizarDevolucao() {
        List<Emprestimo> abertos = empDAO.buscarEmprestimosAbertosPorUsuario(usuarioLogado.getId());

        if (abertos.isEmpty()) {
            view.exibirMensagem("Você não possui empréstimos em aberto.");
            return;
        }

        view.exibirEmprestimos(abertos);
        int empId = view.lerIdEmprestimo();
        Emprestimo emp = empDAO.buscarPorId(empId);

        if (emp == null) {
            view.exibirMensagem("Empréstimo não encontrado.");
            return;
        }

        boolean houveMulata = emp.registrarDevolucao();
        empDAO.atualizar(emp);
        view.exibirResultadoDevolucao(emp);

        if (houveMulata) {
            usuarioLogado = usuarioDAO.buscarPorMatricula(usuarioLogado.getMatricula());
        }
    }

    private void consultarHistorico() {
        List<Emprestimo> historico = empDAO.buscarHistoricoPorUsuario(usuarioLogado.getId());
        view.exibirHistorico(historico, usuarioLogado.getNome());
    }

    private void listarTodosEmprestimos() {
        List<Emprestimo> todos = empDAO.buscarTodos();
        view.exibirEmprestimos(todos);
    }

    private void pagarMulta() {
        usuarioLogado = usuarioDAO.buscarPorMatricula(usuarioLogado.getMatricula());

        if (!usuarioLogado.temMultaPendente()) {
            view.exibirMensagem("Nenhuma multa pendente para o seu cadastro.");
            return;
        }

        double valor = usuarioLogado.getMultaPendente();
        usuarioLogado.pagarMulta();
        usuarioDAO.atualizar(usuarioLogado);
        view.exibirMensagem("Multa de R$ " + valor + " paga com sucesso!");
    }
}
