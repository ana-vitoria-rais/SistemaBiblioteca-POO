package br.edu.excecao;

public class LivroNaoEncontradoException extends Exception {

    public LivroNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
