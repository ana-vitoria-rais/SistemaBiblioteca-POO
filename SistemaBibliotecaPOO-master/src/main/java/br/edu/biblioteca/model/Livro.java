package br.edu.biblioteca.model;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private boolean disponivel;

    public Livro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = true;
    }

    public Livro(int id, String titulo, String autor, boolean disponivel) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = disponivel;
    }

    public void retirar() {
        this.disponivel = false;
    }

    public void devolver() {
        this.disponivel = true;
    }

    public int getId()           { return id; }
    public String getTitulo()    { return titulo; }
    public String getAutor()     { return autor; }
    public boolean ehDisponivel(){ return disponivel; }

    public void setId(int id)              { this.id = id; }
    public void setTitulo(String titulo)   { this.titulo = titulo; }
    public void setAutor(String autor)     { this.autor = autor; }
    public void setDisponivel(boolean d)   { this.disponivel = d; }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo + " - " + autor
                + (disponivel ? " [Disponível]" : " [Indisponível]");
    }
}
