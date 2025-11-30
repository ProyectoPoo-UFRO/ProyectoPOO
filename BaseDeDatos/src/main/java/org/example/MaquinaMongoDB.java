package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Scanner;
import static com.mongodb.client.model.Updates.set;

public class MaquinaMongoDB {

    private static final String CONNECTION_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "maquinaExpendedoraDB";
    private static final String COLLECTION_PRODUCTOS = "productos";
    private static final String COLLECTION_VENTAS = "ventas";
    private static final String COLLECTION_REPOSICIONES = "reposiciones";


    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_URI)) {

            MongoDatabase db = mongoClient.getDatabase(DATABASE_NAME);

            // Colecciones para POJOs (trabajan con Document)
            MongoCollection<Document> productosCollection = db.getCollection(COLLECTION_PRODUCTOS);
            MongoCollection<Document> ventasCollection = db.getCollection(COLLECTION_VENTAS);
            MongoCollection<Document> reposicionesCollection = db.getCollection(COLLECTION_REPOSICIONES);

            System.out.println("Conexión exitosa a MongoDB. Base de datos: " + DATABASE_NAME);

            if (productosCollection.countDocuments() == 0) {
                cargarDatosIniciales(productosCollection);
            }

            ejecutarMenu(productosCollection, ventasCollection, reposicionesCollection);

        } catch (Exception e) {
            System.err.println("Error de conexión a MongoDB. Asegúrate de que el servidor esté corriendo.");
            e.printStackTrace();
        }
    }

    private static void cargarDatosIniciales(MongoCollection<Document> productosCollection) {
        System.out.println("Cargando datos de ejemplo...");

        Producto soda = new Producto(
                "Coca Cola",
                700.00,
                "Bebida",
                "A1",
                5,
                "cola.jpg"
        );

        productosCollection.insertOne(Mapper.productoToDocument(soda));

        System.out.println("Producto inicial 'Coca Cola' insertado con éxito.");
    }

    private static void ejecutarMenu(
            MongoCollection<Document> productosCollection,
            MongoCollection<Document> ventasCollection,
            MongoCollection<Document> reposicionesCollection) {

        Scanner scan = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== MENÚ MÁQUINA EXPENDEDORA ===");
            System.out.println("1. Listar productos disponibles");
            System.out.println("2. Vender producto (Compra)");
            System.out.println("3. Reabastecer stock (Admin) y registrar reposición");
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
                case 2 -> venderProducto(productosCollection, ventasCollection, scan);
                case 3 -> reabastecerStock(productosCollection, reposicionesCollection, scan);
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
                    // Convertimos Document a Producto (POJO) para la visualización
                    Producto p = Mapper.documentToProducto(doc);
                    System.out.println(p.toString());
                });
    }

    private static void venderProducto(
            MongoCollection<Document> productosCollection,
            MongoCollection<Document> ventasCollection,
            Scanner scan) {

        System.out.print("Ingresa el código del slot a comprar (ej. A1): ");
        String codigo = scan.nextLine().toUpperCase();

        Document productoDoc = productosCollection.find(Filters.eq("codigoSlot", codigo)).first();

        if (productoDoc == null) {
            System.out.println("Error: El código de slot no existe.");
            return;
        }

        Producto producto = Mapper.documentToProducto(productoDoc);

        if (producto.getStock() <= 0) {
            System.out.println("Producto agotado.");
            return;
        }

        System.out.print("Precio del producto: $" + producto.getPrecio() + ". Ingrese el monto pagado: ");
        if (!scan.hasNextDouble()) {
            System.out.println("Error: Monto inválido.");
            scan.nextLine();
            return;
        }
        double montoPagado = scan.nextDouble();
        scan.nextLine();

        if (montoPagado < producto.getPrecio()) {
            System.out.println("Monto insuficiente.");
            return;
        }

        double vuelto = montoPagado - producto.getPrecio();

        int nuevoStock = producto.getStock() - 1;
        productosCollection.updateOne(
                Filters.eq("codigoSlot", codigo),
                set("stock", nuevoStock)
        );

        Venta nuevaVenta = new Venta(
                producto.getId(),
                1,
                montoPagado,
                vuelto
        );
        ventasCollection.insertOne(Mapper.ventaToDocument(nuevaVenta));

        System.out.println("¡Venta exitosa! " + producto.getNombre() + " entregado.");
        System.out.printf("Su vuelto es de: $%.2f%n", vuelto);
        System.out.println("Stock restante: " + nuevoStock);
    }

    private static void reabastecerStock(
            MongoCollection<Document> productosCollection,
            MongoCollection<Document> reposicionesCollection,
            Scanner scan) {

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

        System.out.print("Ingresa el nombre del técnico/administrador: ");
        String tecnico = scan.nextLine();


        Document productoDoc = productosCollection.find(Filters.eq("codigoSlot", codigo)).first();

        if (productoDoc == null) {
            System.out.println("Error: El código de slot no existe.");
            return;
        }

        Producto producto = Mapper.documentToProducto(productoDoc);
        ObjectId productoId = producto.getId();


        int nuevoStock = producto.getStock() + cantidad;

        productosCollection.updateOne(
                Filters.eq("codigoSlot", codigo),
                set("stock", nuevoStock)
        );

        Reposicion nuevaReposicion = new Reposicion(
                productoId,
                cantidad,
                tecnico
        );
        reposicionesCollection.insertOne(Mapper.reposicionToDocument(nuevaReposicion));

        System.out.println("Reabastecimiento exitoso para " + producto.getNombre() + ". Nuevo stock: " + nuevoStock);
        System.out.println("Reposición registrada por: " + tecnico);
    }
}