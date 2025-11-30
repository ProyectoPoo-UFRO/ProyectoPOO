package org.example;

import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private MaquinaMongoDB mongo;
    private Scanner sc;

    public MenuPrincipal() {
        mongo = new MaquinaMongoDB();
        sc = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;

        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Ver todas las máquinas");
            System.out.println("2. Insertar máquinas de ejemplo");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> mostrarTodasLasMaquinas();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void mostrarTodasLasMaquinas() {
        List<MaquinaExpendedora> maquinas = mongo.obtenerTodasLasMaquinas();

        if (maquinas.isEmpty()) {
            System.out.println("No hay máquinas registradas.");
            return;
        }

        System.out.println("\n===== LISTA DE MÁQUINAS =====");
        for (MaquinaExpendedora m : maquinas) {
            System.out.println(m.toString());
        }
    }

}
