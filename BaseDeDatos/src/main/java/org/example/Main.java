package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<MaquinaExpendedora> maquinas = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int op;

        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Cliente");
            System.out.println("2. Técnico");
            System.out.println("3. Administrador");
            System.out.println("0. Salir");
            System.out.print("Seleccione opción: ");
            op = sc.nextInt();

            switch (op) {
                case 1 -> menuCliente();
                case 2 -> menuTecnico();
                case 3 -> menuAdmin();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }

    // ---- MÉTODO NUEVO PARA LEER DOUBLE ----
    private static double leerDouble(String mensaje) {
        System.out.print(mensaje);
        String txt = sc.next().replace(",", ".");
        return Double.parseDouble(txt);
    }
    // ----------------------------------------


    // ========== MENÚ TÉCNICO ==========
    private static void menuTecnico() {
        int op;

        do {
            System.out.println("\n===== MENU TECNICO =====");
            System.out.println("1. Registrar máquina");
            System.out.println("2. Ver todas las máquinas");
            System.out.println("3. Agregar producto a máquina");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            op = sc.nextInt();

            switch (op) {
                case 1 -> registrarMaquina();
                case 2 -> listarMaquinas();
                case 3 -> agregarProductoMaquina();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }


    // ========== REGISTRAR MÁQUINA ==========
    private static void registrarMaquina() {
        System.out.println("\n--- Registrar Máquina ---");

        System.out.print("Ingrese ID: ");
        String id = sc.next();

        System.out.print("Ingrese estado: ");
        String estado = sc.next();

        double lat = leerDouble("Ingrese latitud: ");
        double lng = leerDouble("Ingrese longitud: ");

        Coordenadas c = new Coordenadas(lat, lng);
        MaquinaExpendedora m = new MaquinaExpendedora(id, estado, c);

        maquinas.add(m);

        System.out.println("Máquina creada correctamente!");
    }


    // ========== LISTAR MÁQUINAS ==========
    private static void listarMaquinas() {
        System.out.println("\n--- Todas las máquinas ---");

        if (maquinas.isEmpty()) {
            System.out.println("No hay máquinas registradas.");
            return;
        }

        for (MaquinaExpendedora m : maquinas) {
            System.out.println(m);
        }
    }


    // ========== AGREGAR PRODUCTO ==========
    private static void agregarProductoMaquina() {
        System.out.print("\nIngrese ID de máquina: ");
        String id = sc.next();

        MaquinaExpendedora maquina = null;

        for (MaquinaExpendedora m : maquinas) {
            if (m.getId().equals(id)) {
                maquina = m;
                break;
            }
        }

        if (maquina == null) {
            System.out.println("Máquina no encontrada.");
            return;
        }

        System.out.print("Nombre producto: ");
        String nombre = sc.next();

        System.out.print("Stock: ");
        int stock = sc.nextInt();

        System.out.print("Precio: ");
        int precio = sc.nextInt();

        Producto p = new Producto(nombre, stock, precio);
        maquina.agregarProducto(p);

        System.out.println("Producto agregado!");
    }


    // ========== MENÚ CLIENTE ==========
    private static void menuCliente() {
        System.out.println("\n(Menú cliente aún simple, si quieres lo mejoro más)");
    }

    // ========== MENÚ ADMIN ==========
    private static void menuAdmin() {
        System.out.println("\n(Menú admin aún simple, lo puedo mejorar si quieres)");
    }
}
