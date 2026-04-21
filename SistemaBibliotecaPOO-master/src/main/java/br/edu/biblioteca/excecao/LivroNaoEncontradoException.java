package br.edu.biblioteca.excecao;

public class LivroNaoEncontradoException extends Exception {

    public LivroNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
