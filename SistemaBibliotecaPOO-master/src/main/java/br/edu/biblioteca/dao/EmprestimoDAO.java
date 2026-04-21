package br.edu.biblioteca.dao;

import br.edu.biblioteca.model.Emprestimo;
import java.util.List;

public interface EmprestimoDAO extends DAO<Emprestimo> {

    List<Emprestimo> buscarEmprestimosAbertosPorUsuario(int usuarioId);

    List<Emprestimo> buscarHistoricoPorUsuario(int usuarioId);
}
