package br.edu.biblioteca;

import br.edu.biblioteca.controller.BibliotecaController;
import br.edu.biblioteca.view.BibliotecaView;

public class Main {
    public static void main(String[] args) {
        BibliotecaView view = new BibliotecaView();
        BibliotecaController controller = new BibliotecaController(view);
        controller.iniciar();
    }
}
