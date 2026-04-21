package br.edu.usuario;

import java.util.ArrayList;

public class Usuario {

    private String nome;
    private String matricula;
    private double multaPendente;
    private ArrayList<String> historicoEmprestimos;

    public Usuario(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
        this.multaPendente = 0.0;
        this.historicoEmprestimos = new ArrayList<>();
    }

    public void adicionarMulta(double valor) {
        multaPendente += valor;
    }

    public void pagarMulta() {
        if (multaPendente == 0) {
            System.out.println("Nenhuma multa pendente.");
        } else {
            System.out.println("Multa de R$ " + multaPendente + " paga com sucesso.");
            multaPendente = 0;
        }
    }

    public void adicionarHistorico(String registro) {
        historicoEmprestimos.add(registro);
    }

    public void exibirHistorico() {
        System.out.println("=== HISTÓRICO DE EMPRÉSTIMOS DE " + nome.toUpperCase() + " ===");
        if (historicoEmprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
        } else {
            for (int i = 0; i < historicoEmprestimos.size(); i++) {
                System.out.println((i + 1) + ". " + historicoEmprestimos.get(i));
            }
        }
    }

    public boolean temMultaPendente() {
        return multaPendente > 0;
    }

    public double getMultaPendente() {
        return multaPendente;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }
}