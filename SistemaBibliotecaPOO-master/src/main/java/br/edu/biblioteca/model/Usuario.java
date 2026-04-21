package br.edu.biblioteca.model;

public class Usuario {

    private int id;
    private String nome;
    private String matricula;
    private double multaPendente;

    public Usuario(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
        this.multaPendente = 0.0;
    }

    public Usuario(int id, String nome, String matricula, double multaPendente) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.multaPendente = multaPendente;
    }

    public void adicionarMulta(double valor) {
        multaPendente += valor;
    }

    public void pagarMulta() {
        multaPendente = 0;
    }

    public boolean temMultaPendente() {
        return multaPendente > 0;
    }

    public int getId()                { return id; }
    public String getNome()           { return nome; }
    public String getMatricula()      { return matricula; }
    public double getMultaPendente()  { return multaPendente; }

    public void setId(int id)                        { this.id = id; }
    public void setNome(String nome)                 { this.nome = nome; }
    public void setMatricula(String matricula)       { this.matricula = matricula; }
    public void setMultaPendente(double multa)       { this.multaPendente = multa; }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " (Matrícula: " + matricula + ")"
                + (multaPendente > 0 ? " | Multa: R$ " + multaPendente : "");
    }
}
