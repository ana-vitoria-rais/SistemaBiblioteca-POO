package br.edu.biblioteca.dao.impl;

import br.edu.biblioteca.dao.LivroDAO;
import br.edu.biblioteca.database.ConnectionFactory;
import br.edu.biblioteca.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LivroDAOImpl implements LivroDAO {

    @Override
    public void salvar(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, disponivel) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setBoolean(3, livro.ehDisponivel());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                livro.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar livro: " + e.getMessage());
        }
    }

    @Override
    public Livro buscarPorId(int id) {
        String sql = "SELECT * FROM livros WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearLivro(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livro: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Livro> buscarTodos() {
        String sql = "SELECT * FROM livros ORDER BY titulo";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livros: " + e.getMessage());
        }
        return removerDuplicados(livros);
    }

    @Override
    public List<Livro> buscarDisponiveis() {
        String sql = "SELECT * FROM livros WHERE disponivel = 1 ORDER BY titulo";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livros disponÃ­veis: " + e.getMessage());
        }
        return removerDuplicados(livros);
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        String sql = "SELECT * FROM livros WHERE titulo LIKE ?";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livro por tÃ­tulo: " + e.getMessage());
        }
        return removerDuplicados(livros);
    }

    @Override
    public void atualizar(Livro livro) {
        String sql = "UPDATE livros SET titulo = ?, autor = ?, disponivel = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setBoolean(3, livro.ehDisponivel());
            stmt.setInt(4, livro.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar livro: " + e.getMessage());
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar livro: " + e.getMessage());
        }
    }

    private Livro mapearLivro(ResultSet rs) throws SQLException {
        return new Livro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getBoolean("disponivel")
        );
    }

    private List<Livro> removerDuplicados(List<Livro> livros) {
        LinkedHashMap<String, Livro> unicos = new LinkedHashMap<>();
        for (Livro livro : livros) {
            String chave = livro.getTitulo() + "|" + livro.getAutor();
            Livro existente = unicos.get(chave);

            if (existente == null || (!existente.ehDisponivel() && livro.ehDisponivel())) {
                unicos.put(chave, livro);
            }
        }
        return new ArrayList<>(unicos.values());
    }
}
