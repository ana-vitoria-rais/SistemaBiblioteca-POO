package br.edu.biblioteca.excecao;

public class LivroIndisponivelException extends Exception {

    public LivroIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
