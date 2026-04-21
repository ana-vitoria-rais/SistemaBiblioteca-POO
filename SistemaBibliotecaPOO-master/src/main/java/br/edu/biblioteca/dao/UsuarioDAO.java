package br.edu.biblioteca.dao;

import br.edu.biblioteca.model.Usuario;

public interface UsuarioDAO extends DAO<Usuario> {

    Usuario buscarPorMatricula(String matricula);
}
