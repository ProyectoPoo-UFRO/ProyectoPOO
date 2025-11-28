package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.Scanner;
import static com.mongodb.client.model.Updates.set;

public class MaquinaMongoDB {

    private static final String CONNECTION_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "maquinaExpendedoraDB";
    private static final String COLLECTION_NAME = "productos";


    public static void main(String[] args) {
        try (MongoClient mongoCliente = MongoClients.create(CONNECTION_URI)) {

            MongoDatabase db = mongoCliente.getDatabase(DATABASE_NAME);
            MongoCollection<Document> productosCollection = db.getCollection(COLLECTION_NAME);

            System.out.println("Conexión exitosa a MongoDB. Base de datos: " + DATABASE_NAME);

            if (productosCollection.countDocuments() == 0) {
                cargarDatosIniciales(productosCollection);
            }

            ejecutarMenu(productosCollection);

        } catch (Exception e) {
            System.err.println("Error de conexión a MongoDB. Asegúrate de que el servidor esté corriendo.");
            e.printStackTrace();
        }
    }

    private static void cargarDatosIniciales(MongoCollection<Document> productosCollection) {
        System.out.println("Cargando datos de ejemplo...");
        Document soda = new Document("codigoSlot", "A1")
                .append("nombre", "Coca Cola")
                .append("precio", 700.00)
                .append("stock", 5);
        productosCollection.insertOne(soda);
        System.out.println("Producto inicial 'Coca Cola' insertado con éxito.");
    }

    private static void ejecutarMenu(MongoCollection<Document> productosCollection) {
        Scanner scan = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== MENÚ MÁQUINA EXPENDEDORA ===");
            System.out.println("1. Listar productos disponibles");
            System.out.println("2. Vender producto (Compra)");
            System.out.println("3. Reabastecer stock (Admin)");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");

            if (scan.hasNextInt()) {
                opcion = scan.nextInt();
                scan.nextLine();
            } else {
                System.out.println("Opción inválida. Intenta de nuevo.");
                scan.nextLine();
                opcion = 0;
            }

            switch (opcion) {
                case 1 -> listarProductos(productosCollection);
                case 2 -> venderProducto(productosCollection, scan);
                case 3 -> reabastecerStock(productosCollection, scan);
                case 4 -> System.out.println("Saliendo del programa...");
                default -> {
                    if (opcion != 0) {
                        System.out.println("Opción no reconocida.");
                    }
                }
            }
        } while (opcion != 4);
        scan.close();
    }

    private static void listarProductos(MongoCollection<Document> productosCollection) {
        System.out.println("\n--- PRODUCTOS DISPONIBLES ---");
        productosCollection.find(Filters.gt("stock", 0))
                .forEach(doc -> {
                    System.out.println("Slot: " + doc.getString("codigoSlot") +
                            " | Nombre: " + doc.getString("nombre") +
                            " | Precio: $" + doc.getDouble("precio") +
                            " | Stock: " + doc.getInteger("stock"));
                });
    }

    private static void venderProducto(MongoCollection<Document> productosCollection, Scanner scan) {
        System.out.print("Ingresa el código del slot a comprar (ej. A1): ");
        String codigo = scan.nextLine().toUpperCase();

        Document producto = productosCollection.find(Filters.eq("codigoSlot", codigo)).first();

        if (producto == null) {
            System.out.println("Error: El código de slot no existe.");
            return;
        }

        int stockActual = producto.getInteger("stock");
        if (stockActual <= 0) {
            System.out.println("Producto agotado.");
            return;
        }

        productosCollection.updateOne(
                Filters.eq("codigoSlot", codigo),
                set("stock", stockActual - 1)
        );

        System.out.println("¡Venta exitosa! " + producto.getString("nombre") + " entregado.");
        System.out.println("Stock restante: " + (stockActual - 1));
    }

    private static void reabastecerStock(MongoCollection<Document> productosCollection, Scanner scan) {
        System.out.print("Ingresa el código del slot a reabastecer: ");
        String codigo = scan.nextLine().toUpperCase();

        System.out.print("Ingresa la cantidad a agregar al stock: ");
        if (!scan.hasNextInt()) {
            System.out.println("Entrada no válida.");
            scan.nextLine();
            return;
        }
        int cantidad = scan.nextInt();
        scan.nextLine();

        Document producto = productosCollection.find(Filters.eq("codigoSlot", codigo)).first();

        if (producto == null) {
            System.out.println("Error: El código de slot no existe. Crea un producto nuevo primero.");
            return;
        }

        int stockActual = producto.getInteger("stock");
        int nuevoStock = stockActual + cantidad;

        productosCollection.updateOne(
                Filters.eq("codigoSlot", codigo),
                set("stock", nuevoStock)
        );

        System.out.println("Reabastecimiento exitoso para " + producto.getString("nombre") + ". Nuevo stock: " + nuevoStock);
    }
}