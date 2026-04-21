package br.edu.biblioteca.dao;

import java.util.List;

public interface DAO<T> {

    void salvar(T objeto);

    T buscarPorId(int id);

    List<T> buscarTodos();

    void atualizar(T objeto);

    void deletar(int id);
}
