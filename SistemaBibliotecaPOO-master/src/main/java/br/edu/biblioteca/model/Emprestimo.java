package br.edu.biblioteca.model;

import java.time.LocalDate;

public class Emprestimo {

    private static final int DIAS_PERMITIDOS = 7;
    private static final double MULTA_POR_DIA = 2.0;

    private int id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataRealDevolucao;
    private int diasAtraso;
    private double multa;

    public Emprestimo(Livro livro, Usuario usuario) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(DIAS_PERMITIDOS);
        this.diasAtraso = 0;
        this.multa = 0.0;
    }

    public Emprestimo(int id, Livro livro, Usuario usuario,
                      LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista,
                      LocalDate dataRealDevolucao, int diasAtraso, double multa) {
        this.id = id;
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataRealDevolucao = dataRealDevolucao;
        this.diasAtraso = diasAtraso;
        this.multa = multa;
    }


    public boolean registrarDevolucao() {
        this.dataRealDevolucao = LocalDate.now();
        this.diasAtraso = (int) Math.max(0,
                dataRealDevolucao.toEpochDay() - dataDevolucaoPrevista.toEpochDay());
        this.multa = diasAtraso * MULTA_POR_DIA;

        if (multa > 0) {
            usuario.adicionarMulta(multa);
        }
        livro.devolver();
        return multa > 0;
    }


    public int getId()                          { return id; }
    public Livro getLivro()                     { return livro; }
    public Usuario getUsuario()                 { return usuario; }
    public LocalDate getDataEmprestimo()        { return dataEmprestimo; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public LocalDate getDataRealDevolucao()     { return dataRealDevolucao; }
    public int getDiasAtraso()                  { return diasAtraso; }
    public double getMulta()                    { return multa; }


    public void setId(int id)                               { this.id = id; }
    public void setDataRealDevolucao(LocalDate d)           { this.dataRealDevolucao = d; }
    public void setDiasAtraso(int diasAtraso)               { this.diasAtraso = diasAtraso; }
    public void setMulta(double multa)                      { this.multa = multa; }

    @Override
    public String toString() {
        String status = (dataRealDevolucao == null) ? "Em aberto" : "Devolvido em " + dataRealDevolucao;
        return "Empréstimo #" + id
                + " | Livro: " + livro.getTitulo()
                + " | Usuário: " + usuario.getNome()
                + " | Emprestado em: " + dataEmprestimo
                + " | Devolução prevista: " + dataDevolucaoPrevista
                + " | Status: " + status
                + (multa > 0 ? " | Multa: R$ " + multa : "");
    }
}
