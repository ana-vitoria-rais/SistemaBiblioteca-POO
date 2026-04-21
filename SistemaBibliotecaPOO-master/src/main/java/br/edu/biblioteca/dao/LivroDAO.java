package br.edu.biblioteca.dao;

import br.edu.biblioteca.model.Livro;
import java.util.List;

public interface LivroDAO extends DAO<Livro> {

    List<Livro> buscarDisponiveis();

    List<Livro> buscarPorTitulo(String titulo);
}
