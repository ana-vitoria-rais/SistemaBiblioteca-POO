package br.edu.biblioteca;

public class Livro {

    private static int contadorId = 1;

    private int id;
    private String titulo;
    private boolean disponivel;

    public Livro(String titulo) {
        this.id = contadorId++;
        this.titulo = titulo;
        this.disponivel = true;
    }

    public void retirar() {
        if (disponivel) {
            disponivel = false;
            System.out.println("Livro retirado com sucesso.");
        } else {
            System.out.println("Livro indisponível.");
        }
    }

    public void devolver() {
        disponivel = true;
        System.out.println("Livro devolvido e disponível novamente.");
    }

    public boolean ehDisponivel() {
        return disponivel;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }
}