package br.edu.biblioteca;

import br.edu.excecao.LivroIndisponivelException;
import br.edu.excecao.MultaPendenteException;
import br.edu.usuario.Usuario;

public class Emprestimo {

    private final int diasPermitidos = 7;
    private final double multaPorDia = 2.0;

    private int dataEmprestimo;
    private int dataDevolucao;

    public void emprestarLivro(Livro livro, Usuario usuario, int diaAtual) throws LivroIndisponivelException, MultaPendenteException {

        if (usuario.temMultaPendente()) {
            throw new MultaPendenteException("O cliente " + usuario.getNome() + " possui multa pendente de R$ " + usuario.getMultaPendente() + ". Efetue o pagamento antes de realizar um novo empréstimo.");
        }

        if (!livro.ehDisponivel()) {
            throw new LivroIndisponivelException("O livro \"" + livro.getTitulo() + "\" não está disponível para empréstimo.");
        }

        dataEmprestimo = diaAtual;
        dataDevolucao = diaAtual + diasPermitidos;

        livro.retirar();
        usuario.adicionarHistorico("Empréstimo - Livro: " + livro.getTitulo() + " | Dia " + dataEmprestimo + " | Devolução prevista: Dia " + dataDevolucao);
        exibirResultadoEmprestimo(livro, usuario);
    }

    public void exibirResultadoEmprestimo(Livro livro, Usuario usuario) {
        System.out.println("ID do Livro: " + livro.getId());
        System.out.println("Usuário: " + usuario.getNome());
        System.out.println("Livro: " + livro.getTitulo());
        System.out.println("Data do empréstimo: Dia " + dataEmprestimo);
        System.out.println("Data prevista para devolução: Dia " + dataDevolucao);
    }

    public void devolverLivro(Livro livro, Usuario usuario, int diaDevolucaoReal) {

        int diasAtraso = Math.max(0, diaDevolucaoReal - dataDevolucao);
        double multa = diasAtraso * multaPorDia;

        livro.devolver();

        if (multa > 0) {
            usuario.adicionarMulta(multa);
        }

        usuario.adicionarHistorico("Devolução - Livro: " + livro.getTitulo() + " | Dia " + diaDevolucaoReal + (diasAtraso > 0 ? " | Atraso: " + diasAtraso + " dia(s) | Multa: R$ " + multa : " | No prazo"));
        exibirResultadoDevolucao(livro, diaDevolucaoReal, diasAtraso, multa);
    }

    public void exibirResultadoDevolucao(Livro livro, int diaDevolucaoReal, int diasAtraso, double multa) {
        System.out.println("ID do Livro: " + livro.getId());
        System.out.println("Livro: " + livro.getTitulo());
        System.out.println("Data real da devolução: Dia " + diaDevolucaoReal);

        if (diasAtraso > 0) {
            System.out.println("Livro devolvido com atraso.");
            System.out.println("Dias de atraso: " + diasAtraso);
            System.out.println("Multa: R$ " + multa);
        } else {
            System.out.println("Livro devolvido no prazo.Obrigado!");
        }
    }
}