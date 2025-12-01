package org.example;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private static MaquinaMongoDB mongo = new MaquinaMongoDB();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int opcion;

        do {
            System.out.println("\n=========== MENU PRINCIPAL ===========");
            System.out.println("1. Modo Cliente");
            System.out.println("2. Modo Técnico");
            System.out.println("3. Modo Administrador");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> menuCliente();
                case 2 -> menuTecnico();
                case 3 -> menuAdmin();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }


    // ============================================================
    //                         MODO CLIENTE
    // ============================================================

    private static void menuCliente() {
        int op;

        do {
            System.out.println("\n----- MODO CLIENTE -----");
            System.out.println("1. Ver máquinas");
            System.out.println("2. Ver productos de una máquina");
            System.out.println("3. Comprar producto");
            System.out.println("0. Volver");
            System.out.print("Opción: ");

            op = leerEntero();

            switch (op) {
                case 1 -> mostrarMaquinas();
                case 2 -> verProductos();
                case 3 -> comprarProducto();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }


    // ============================================================
    //                         MODO TÉCNICO
    // ============================================================

    private static void menuTecnico() {
        int op;

        do {
            System.out.println("\n----- MODO TÉCNICO -----");
            System.out.println("1. Registrar máquina");
            System.out.println("2. Agregar producto");
            System.out.println("3. Modificar stock");
            System.out.println("4. Cambiar estado");
            System.out.println("5. Ver ubicación");
            System.out.println("0. Volver");
            System.out.print("Opción: ");

            op = leerEntero();

            switch (op) {
                case 1 -> registrarMaquina();
                case 2 -> agregarProducto();
                case 3 -> modificarStock();
                case 4 -> cambiarEstado();
                case 5 -> verUbicacion();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }


    // ============================================================
    //                         MODO ADMIN
    // ============================================================

    private static void menuAdmin() {
        int op;

        do {
            System.out.println("\n----- MODO ADMIN -----");
            System.out.println("1. Ver todas las máquinas");
            System.out.println("2. Ver historial de ventas");
            System.out.println("0. Volver");
            System.out.print("Opción: ");

            op = leerEntero();

            switch (op) {
                case 1 -> mostrarMaquinas();
                case 2 -> System.out.println("Historial de ventas aún no implementado 😅");
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }


    // ============================================================
    //                   FUNCIONES DEL SISTEMA
    // ============================================================

    private static void registrarMaquina() {

        System.out.print("ID: ");
        String id = sc.nextLine();

        System.out.print("Estado (Activa/Inactiva): ");
        String estado = sc.nextLine();

        System.out.print("Latitud: ");
        double lat = leerDouble();

        System.out.print("Longitud: ");
        double lng = leerDouble();

        Coordenadas c = new Coordenadas(lat, lng);
        MaquinaExpendedora m = new MaquinaExpendedora(id, estado, c);

        mongo.insertarMaquina(m);
        System.out.println("Máquina registrada.");
    }

    private static void mostrarMaquinas() {
        List<Document> docs = mongo.obtenerMaquinas();

        if (docs.isEmpty()) {
            System.out.println("No hay máquinas.");
            return;
        }

        System.out.println("\n=== MÁQUINAS ===");
        for (Document d : docs) {
            System.out.println(d.toJson());
        }
    }

    private static void verProductos() {

        System.out.print("ID: ");
        String id = sc.nextLine();

        MaquinaExpendedora m = mongo.obtenerMaquina(id);

        if (m == null) {
            System.out.println("No existe la máquina.");
            return;
        }

        if (m.getProductos().isEmpty()) {
            System.out.println("No tiene productos.");
            return;
        }

        System.out.println("\nProductos:");
        for (Producto p : m.getProductos()) System.out.println(p);
    }

    private static void agregarProducto() {

        System.out.print("ID: ");
        String id = sc.nextLine();

        MaquinaExpendedora m = mongo.obtenerMaquina(id);
        if (m == null) {
            System.out.println("No existe.");
            return;
        }

        System.out.print("Nombre producto: ");
        String nombre = sc.nextLine();

        System.out.print("Stock: ");
        int stock = leerEntero();

        System.out.print("Precio: ");
        int precio = leerEntero();

        m.agregarProducto(new Producto(nombre, stock, precio));
        mongo.actualizarProductos(id, m.getProductos());

        System.out.println("Producto agregado.");
    }

    private static void modificarStock() {
        System.out.print("ID: ");
        String id = sc.nextLine();

        MaquinaExpendedora m = mongo.obtenerMaquina(id);
        if (m == null) {
            System.out.println("No existe.");
            return;
        }

        System.out.print("Producto a modificar: ");
        String nombre = sc.nextLine();

        for (Producto p : m.getProductos()) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                System.out.print("Nuevo stock: ");
                p.setStock(leerEntero());

                mongo.actualizarProductos(id, m.getProductos());
                System.out.println("Stock modificado.");
                return;
            }
        }

        System.out.println("Producto no encontrado.");
    }

    private static void cambiarEstado() {
        System.out.print("ID: ");
        String id = sc.nextLine();

        MaquinaExpendedora m = mongo.obtenerMaquina(id);
        if (m == null) {
            System.out.println("No existe.");
            return;
        }

        System.out.print("Nuevo estado: ");
        String estado = sc.nextLine();

        m.setEstado(estado);
        mongo.actualizarEstado(id, estado);

        System.out.println("Estado actualizado.");
    }

    private static void verUbicacion() {
        System.out.print("ID: ");
        String id = sc.nextLine();

        MaquinaExpendedora m = mongo.obtenerMaquina(id);
        if (m == null) {
            System.out.println("No existe.");
            return;
        }

        System.out.println("Ubicación: " + m.getUbicacion());
    }

    private static void comprarProducto() {

        System.out.print("ID: ");
        String id = sc.nextLine();

        MaquinaExpendedora m = mongo.obtenerMaquina(id);
        if (m == null) {
            System.out.println("No existe.");
            return;
        }

        System.out.print("Producto a comprar: ");
        String nombre = sc.nextLine();

        for (Producto p : m.getProductos()) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {

                if (p.getStock() <= 0) {
                    System.out.println("Sin stock.");
                    return;
                }

                System.out.println("Precio: $" + p.getPrecio());
                System.out.print("Ingresa dinero: ");
                int dinero = leerEntero();

                if (dinero < p.getPrecio()) {
                    System.out.println("Dinero insuficiente.");
                    return;
                }

                p.setStock(p.getStock() - 1);
                mongo.actualizarProductos(id, m.getProductos());

                System.out.println("Compra realizada. Vuelto: $" + (dinero - p.getPrecio()));
                return;
            }
        }

        System.out.println("Producto no encontrado.");
    }


    // ============================================================
    //                     FUNCIONES AUXILIARES
    // ============================================================

    private static int leerEntero() {
        while (!sc.hasNextInt()) sc.next();
        int n = sc.nextInt();
        sc.nextLine();
        return n;
    }

    private static double leerDouble() {
        while (!sc.hasNextDouble()) sc.next();
        double n = sc.nextDouble();
        sc.nextLine();
        return n;
    }
}
