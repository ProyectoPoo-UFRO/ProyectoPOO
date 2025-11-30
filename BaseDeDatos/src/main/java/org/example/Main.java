package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<MaquinaExpendedora> maquinas = new ArrayList<>();
    private static ArrayList<String> historialVentas = new ArrayList<>();
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

    // ========== LECTOR DE DOUBLE SEGURO ==========
    private static double leerDouble(String mensaje) {
        System.out.print(mensaje);
        String txt = sc.next().replace(",", ".");
        return Double.parseDouble(txt);
    }


    // =================== MENÚ CLIENTE ===================
    private static void menuCliente() {
        int op;

        do {
            System.out.println("\n===== MENU CLIENTE =====");
            System.out.println("1. Ver máquinas");
            System.out.println("2. Comprar producto");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            op = sc.nextInt();

            switch (op) {
                case 1 -> listarMaquinas();
                case 2 -> comprarProducto();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }


    // ========== COMPRA DE PRODUCTO ==========
    private static void comprarProducto() {
        System.out.print("\nIngrese ID de máquina: ");
        String id = sc.next();

        MaquinaExpendedora maquina = buscarMaquina(id);

        if (maquina == null) {
            System.out.println("Máquina no encontrada.");
            return;
        }

        if (maquina.getProductos().isEmpty()) {
            System.out.println("No hay productos.");
            return;
        }

        System.out.println("\n--- Productos disponibles ---");
        for (int i = 0; i < maquina.getProductos().size(); i++) {
            System.out.println((i + 1) + ". " + maquina.getProductos().get(i));
        }

        System.out.print("Seleccione producto: ");
        int index = sc.nextInt() - 1;

        if (index < 0 || index >= maquina.getProductos().size()) {
            System.out.println("Índice inválido.");
            return;
        }

        Producto p = maquina.getProductos().get(index);

        if (p.getStock() <= 0) {
            System.out.println("No queda stock.");
            return;
        }

        p.setStock(p.getStock() - 1);
        historialVentas.add("Venta: " + p.getNombre() + " (Máquina " + maquina.getId() + ")");

        System.out.println("Compra realizada! Stock restante: " + p.getStock());
    }


    // =================== MENÚ TÉCNICO ===================
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

        Coordenadas coords = new Coordenadas(lat, lng);

        MaquinaExpendedora nueva = new MaquinaExpendedora(id, estado, coords);
        maquinas.add(nueva);

        System.out.println("Máquina registrada con éxito!");
    }


    // ========== BUSCAR MÁQUINA ==========
    private static MaquinaExpendedora buscarMaquina(String id) {
        for (MaquinaExpendedora m : maquinas) {
            if (m.getId().equals(id)) return m;
        }
        return null;
    }


    // ========== LISTAR MÁQUINAS ==========
    private static void listarMaquinas() {
        System.out.println("\n--- TODAS LAS MÁQUINAS ---");

        if (maquinas.isEmpty()) {
            System.out.println("No hay máquinas.");
            return;
        }

        for (MaquinaExpendedora m : maquinas) {
            System.out.println(m);
        }
    }


    // ========== AGREGAR PRODUCTO A MÁQUINA ==========
    private static void agregarProductoMaquina() {
        System.out.print("\nIngrese ID de máquina: ");
        String id = sc.next();

        MaquinaExpendedora maquina = buscarMaquina(id);

        if (maquina == null) {
            System.out.println("Máquina no encontrada.");
            return;
        }

        System.out.print("Nombre del producto: ");
        String nombre = sc.next();

        System.out.print("Stock inicial: ");
        int stock = sc.nextInt();

        System.out.print("Precio: ");
        int precio = sc.nextInt();

        Producto p = new Producto(nombre, stock, precio);
        maquina.agregarProducto(p);

        System.out.println("Producto agregado con éxito.");
    }


    // =================== MENÚ ADMIN ===================
    private static void menuAdmin() {
        int op;

        do {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("1. Ver historial de ventas");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            op = sc.nextInt();

            switch (op) {
                case 1 -> mostrarHistorial();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }


    // ========== MOSTRAR HISTORIAL ==========
    private static void mostrarHistorial() {
        System.out.println("\n--- HISTORIAL DE VENTAS ---");

        if (historialVentas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        for (String venta : historialVentas) {
            System.out.println(venta);
        }
    }
}