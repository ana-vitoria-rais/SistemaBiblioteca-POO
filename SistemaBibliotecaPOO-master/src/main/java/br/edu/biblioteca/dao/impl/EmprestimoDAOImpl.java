package br.edu.biblioteca.dao.impl;

import br.edu.biblioteca.dao.EmprestimoDAO;
import br.edu.biblioteca.database.ConnectionFactory;
import br.edu.biblioteca.model.Emprestimo;
import br.edu.biblioteca.model.Livro;
import br.edu.biblioteca.model.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class EmprestimoDAOImpl implements EmprestimoDAO {

    @Override
    public void salvar(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimos (livro_id, usuario_id, data_emprestimo, data_devolucao) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, emprestimo.getLivro().getId());
            stmt.setInt(2, emprestimo.getUsuario().getId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucaoPrevista()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                emprestimo.setId(rs.getInt(1));
            }

            atualizarDisponibilidadeLivro(conn, emprestimo.getLivro().getId(), false);

        } catch (SQLException e) {
            System.out.println("Erro ao registrar empréstimo: " + e.getMessage());
        }
    }

    @Override
    public Emprestimo buscarPorId(int id) {
        String sql = "SELECT e.*, " +
                     "l.titulo, l.autor, l.disponivel, " +
                     "u.nome, u.matricula, u.multa_pendente " +
                     "FROM emprestimos e " +
                     "JOIN livros l ON e.livro_id = l.id " +
                     "JOIN usuarios u ON e.usuario_id = u.id " +
                     "WHERE e.id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearEmprestimo(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar empréstimo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Emprestimo> buscarTodos() {
        String sql = "SELECT e.*, " +
                     "l.titulo, l.autor, l.disponivel, " +
                     "u.nome, u.matricula, u.multa_pendente " +
                     "FROM emprestimos e " +
                     "JOIN livros l ON e.livro_id = l.id " +
                     "JOIN usuarios u ON e.usuario_id = u.id " +
                     "ORDER BY e.data_emprestimo DESC";

        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emprestimos.add(mapearEmprestimo(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar empréstimos: " + e.getMessage());
        }
        return emprestimos;
    }

    @Override
    public List<Emprestimo> buscarEmprestimosAbertosPorUsuario(int usuarioId) {
        String sql = "SELECT e.*, " +
                     "l.titulo, l.autor, l.disponivel, " +
                     "u.nome, u.matricula, u.multa_pendente " +
                     "FROM emprestimos e " +
                     "JOIN livros l ON e.livro_id = l.id " +
                     "JOIN usuarios u ON e.usuario_id = u.id " +
                     "WHERE e.usuario_id = ? AND e.data_real_dev IS NULL";

        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                emprestimos.add(mapearEmprestimo(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar empréstimos em aberto: " + e.getMessage());
        }
        return emprestimos;
    }

    @Override
    public List<Emprestimo> buscarHistoricoPorUsuario(int usuarioId) {
        String sql = "SELECT e.*, " +
                     "l.titulo, l.autor, l.disponivel, " +
                     "u.nome, u.matricula, u.multa_pendente " +
                     "FROM emprestimos e " +
                     "JOIN livros l ON e.livro_id = l.id " +
                     "JOIN usuarios u ON e.usuario_id = u.id " +
                     "WHERE e.usuario_id = ? " +
                     "ORDER BY e.data_emprestimo DESC";

        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                emprestimos.add(mapearEmprestimo(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar histórico: " + e.getMessage());
        }
        return emprestimos;
    }

    @Override
    public void atualizar(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimos SET data_real_dev = ?, dias_atraso = ?, multa = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (emprestimo.getDataRealDevolucao() != null) {
                stmt.setDate(1, Date.valueOf(emprestimo.getDataRealDevolucao()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            stmt.setInt(2, emprestimo.getDiasAtraso());
            stmt.setDouble(3, emprestimo.getMulta());
            stmt.setInt(4, emprestimo.getId());
            stmt.executeUpdate();

            atualizarDisponibilidadeLivro(conn, emprestimo.getLivro().getId(), true);

            atualizarMultaUsuario(conn, emprestimo.getUsuario());

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar empréstimo: " + e.getMessage());
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM emprestimos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar empréstimo: " + e.getMessage());
        }
    }

    private Emprestimo mapearEmprestimo(ResultSet rs) throws SQLException {
        Livro livro = new Livro(
                rs.getInt("livro_id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getBoolean("disponivel")
        );

        Usuario usuario = new Usuario(
                rs.getInt("usuario_id"),
                rs.getString("nome"),
                rs.getString("matricula"),
                rs.getDouble("multa_pendente")
        );

        Date dataRealDev = rs.getDate("data_real_dev");
        LocalDate dataRealDevolucao = (dataRealDev != null) ? dataRealDev.toLocalDate() : null;

        return new Emprestimo(
                rs.getInt("id"),
                livro,
                usuario,
                rs.getDate("data_emprestimo").toLocalDate(),
                rs.getDate("data_devolucao").toLocalDate(),
                dataRealDevolucao,
                rs.getInt("dias_atraso"),
                rs.getDouble("multa")
        );
    }

    private void atualizarDisponibilidadeLivro(Connection conn, int livroId, boolean disponivel)
            throws SQLException {
        String sql = "UPDATE livros SET disponivel = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setBoolean(1, disponivel);
        stmt.setInt(2, livroId);
        stmt.executeUpdate();
    }

    private void atualizarMultaUsuario(Connection conn, Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET multa_pendente = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDouble(1, usuario.getMultaPendente());
        stmt.setInt(2, usuario.getId());
        stmt.executeUpdate();
    }
}
