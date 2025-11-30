package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

/**
 * Clase principal que gestiona la conexión a MongoDB y la lógica de la Máquina Expendedora.
 * Incluye todas las funciones de gestión de datos, menús y la corrección del ExceptionInInitializerError.
 */
public class MaquinaMongoDB {
    private static final String URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "maquinaExpendedoraDB";

    private MongoClient mongoClient;
    private MongoDatabase database;

    // Colecciones
    private MongoCollection<Document> maquinasCollection;
    private MongoCollection<Document> productosCollection;
    private MongoCollection<Document> inventarioCollection;
    private MongoCollection<Document> usuariosCollection;
    private MongoCollection<Document> ventasCollection;

    private Scanner scanner;
    // ID de la máquina expendedora en la que estamos operando
    private ObjectId maquinaActivaId;
    private String tecnicoLogueado = null; // Para control de sesión

    public MaquinaMongoDB() {
        this.scanner = new Scanner(System.in);
        try {
            mongoClient = MongoClients.create(URI);
            database = mongoClient.getDatabase(DATABASE_NAME);

            maquinasCollection = database.getCollection("maquinas");
            productosCollection = database.getCollection("productos");
            inventarioCollection = database.getCollection("inventario");
            usuariosCollection = database.getCollection("usuarios");
            ventasCollection = database.getCollection("ventas");

            System.out.println("Conexión a MongoDB establecida con éxito.");

            // Inicialización de datos de prueba
            inicializarDatos();

            // ** CORRECCIÓN DEL ERROR DE INICIALIZACIÓN **
            // Esto asegura que maquinaActivaId se inicialice correctamente.
            Document maquinaDoc = maquinasCollection.find().first();
            MaquinaExpendedora maquinaActiva = Mapper.documentToMaquina(maquinaDoc);

            if (maquinaActiva != null) {
                maquinaActivaId = maquinaActiva.getId();
                System.out.println("Máquina activa cargada: " + maquinaActiva.getCodigo() +
                        " (" + maquinaActiva.getUbicacion() + ")");
            } else {
                System.out.println("ADVERTENCIA: No se encontró máquina. Se insertará una por defecto.");
                maquinaActivaId = insertarMaquina(new MaquinaExpendedora("MV-001", -38.735, -72.600, "Campus Central UFRO", "OPERATIVA"));
            }

        } catch (Exception e) {
            System.err.println("Error de conexión a MongoDB. Asegúrese de que el servidor esté corriendo.");
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------
    // MÉTODOS DE GESTIÓN INTERNA (INSERCIÓN)
    // -----------------------------------------------------

    private void inicializarDatos() {
        if (usuariosCollection.countDocuments() == 0) {
            usuariosCollection.insertOne(Mapper.usuarioToDocument(new Usuario("admin", "1234", "admin")));
            usuariosCollection.insertOne(Mapper.usuarioToDocument(new Usuario("tecnico", "5678", "tecnico")));
        }
        if (productosCollection.countDocuments() == 0) {
            insertarProducto(new Producto("Coca Cola 350ml", 1.20, "Bebida", "url_coca.png"));
            insertarProducto(new Producto("Papas Fritas Lays", 0.99, "Snack", "url_lays.png"));
            insertarProducto(new Producto("Jugo Naranja", 1.50, "Bebida", "url_jugo.png"));
        }
    }

    public ObjectId insertarMaquina(MaquinaExpendedora maquina) {
        Document doc = Mapper.maquinaToDocument(maquina);
        maquinasCollection.insertOne(doc);
        System.out.println("Máquina registrada con ID: " + doc.getObjectId("_id"));
        return doc.getObjectId("_id");
    }

    public void registrarMaquina() {
        System.out.println("\n--- REGISTRAR NUEVA MÁQUINA ---");
        System.out.print("Código (ej. MV-002): ");
        String codigo = scanner.nextLine();
        System.out.print("Ubicación: ");
        String ubicacion = scanner.nextLine();
        System.out.print("Estado (ej. OPERATIVA/REPARACION): ");
        String estado = scanner.nextLine();

        double latitud = 0.0, longitud = 0.0;
        try {
            System.out.print("Latitud: ");
            latitud = scanner.nextDouble();
            System.out.print("Longitud: ");
            longitud = scanner.nextDouble();
            scanner.nextLine(); // Limpiar buffer
        } catch (InputMismatchException e) {
            System.out.println("Error: Latitud/Longitud debe ser un número. Registro cancelado.");
            scanner.nextLine();
            return;
        }

        MaquinaExpendedora nuevaMaquina = new MaquinaExpendedora(codigo, latitud, longitud, ubicacion, estado);
        insertarMaquina(nuevaMaquina);
    }

    public void seleccionarMaquina() {
        System.out.println("\n--- SELECCIONAR MÁQUINA ACTIVA ---");
        List<Document> maquinas = new ArrayList<>();
        maquinasCollection.find().forEach(maquinas::add);

        if (maquinas.isEmpty()) {
            System.out.println("No hay máquinas registradas.");
            return;
        }

        System.out.println("Máquinas disponibles:");
        for (int i = 0; i < maquinas.size(); i++) {
            Document doc = maquinas.get(i);
            String activo = doc.getObjectId("_id").equals(maquinaActivaId) ? " (ACTIVA)" : "";
            System.out.println((i + 1) + ". " + doc.getString("codigo") + " - " + doc.getString("ubicacion") + activo);
        }

        System.out.print("Ingresa el número de la máquina a activar: ");
        try {
            int seleccion = scanner.nextInt();
            scanner.nextLine();

            if (seleccion > 0 && seleccion <= maquinas.size()) {
                maquinaActivaId = maquinas.get(seleccion - 1).getObjectId("_id");
                System.out.println("Máquina activa cambiada a: " + maquinas.get(seleccion - 1).getString("codigo"));
            } else {
                System.out.println("Selección inválida.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Operación cancelada.");
            scanner.nextLine();
        }
    }

    public void eliminarMaquina() {
        System.out.println("\n--- ELIMINAR MÁQUINA ---");
        List<Document> maquinas = new ArrayList<>();
        maquinasCollection.find().forEach(maquinas::add);

        if (maquinas.isEmpty()) {
            System.out.println("No hay máquinas registradas para eliminar.");
            return;
        }

        System.out.println("Máquinas disponibles:");
        for (int i = 0; i < maquinas.size(); i++) {
            Document doc = maquinas.get(i);
            System.out.println((i + 1) + ". " + doc.getString("codigo") + " - " + doc.getString("ubicacion"));
        }

        System.out.print("Ingresa el número de la máquina a eliminar: ");
        try {
            int seleccion = scanner.nextInt();
            scanner.nextLine();

            if (seleccion > 0 && seleccion <= maquinas.size()) {
                ObjectId idAEliminar = maquinas.get(seleccion - 1).getObjectId("_id");

                if (idAEliminar.equals(maquinaActivaId)) {
                    System.out.println("ERROR: No puedes eliminar la máquina activa. Por favor, selecciona otra primero.");
                    return;
                }

                // Eliminar la máquina
                DeleteResult resultadoMaquina = maquinasCollection.deleteOne(eq("_id", idAEliminar));
                // Eliminar su inventario asociado
                DeleteResult resultadoInventario = inventarioCollection.deleteMany(eq("maquinaId", idAEliminar));

                if (resultadoMaquina.getDeletedCount() > 0) {
                    System.out.println("Máquina " + maquinas.get(seleccion - 1).getString("codigo") + " eliminada.");
                    System.out.println("Se eliminaron " + resultadoInventario.getDeletedCount() + " items de inventario asociados.");
                } else {
                    System.out.println("Error: Máquina no encontrada o no se pudo eliminar.");
                }
            } else {
                System.out.println("Selección inválida.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Operación cancelada.");
            scanner.nextLine();
        }
    }

    public ObjectId insertarProducto(Producto producto) {
        Document doc = Mapper.productoToDocument(producto);
        productosCollection.insertOne(doc);
        return doc.getObjectId("_id");
    }

    public void registrarProducto() {
        System.out.println("\n--- REGISTRAR NUEVO PRODUCTO ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        System.out.print("URL de Imagen (opcional): ");
        String url = scanner.nextLine();

        double precio = 0.0;
        try {
            System.out.print("Precio: ");
            precio = scanner.nextDouble();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Error: El precio debe ser un número. Registro cancelado.");
            scanner.nextLine();
            return;
        }

        Producto nuevoProducto = new Producto(nombre, precio, categoria, url);
        insertarProducto(nuevoProducto);
        System.out.println("Producto '" + nombre + "' registrado con éxito.");
    }

    public void eliminarProducto() {
        System.out.println("\n--- ELIMINAR PRODUCTO DEL CATÁLOGO ---");
        List<Document> productosCatalogo = new ArrayList<>();
        productosCollection.find().forEach(productosCatalogo::add);

        if (productosCatalogo.isEmpty()) {
            System.out.println("No hay productos en el catálogo para eliminar.");
            return;
        }

        System.out.println("Productos disponibles:");
        for (int i = 0; i < productosCatalogo.size(); i++) {
            Document doc = productosCatalogo.get(i);
            System.out.println((i + 1) + ". " + doc.getString("nombre") + " ($" + doc.getDouble("precio") + ")");
        }

        System.out.print("Ingresa el número del producto a eliminar: ");
        try {
            int seleccion = scanner.nextInt();
            scanner.nextLine();

            if (seleccion > 0 && seleccion <= productosCatalogo.size()) {
                ObjectId idAEliminar = productosCatalogo.get(seleccion - 1).getObjectId("_id");
                String nombreProducto = productosCatalogo.get(seleccion - 1).getString("nombre");

                // 1. Eliminar el producto del catálogo
                DeleteResult resultadoProducto = productosCollection.deleteOne(eq("_id", idAEliminar));

                // 2. Eliminar referencias del inventario (para todas las máquinas)
                DeleteResult resultadoInventario = inventarioCollection.deleteMany(eq("productoId", idAEliminar));

                if (resultadoProducto.getDeletedCount() > 0) {
                    System.out.println("Producto '" + nombreProducto + "' eliminado del catálogo.");
                    System.out.println("Se eliminaron " + resultadoInventario.getDeletedCount() + " referencias de inventario.");
                } else {
                    System.out.println("Error: Producto no encontrado o no se pudo eliminar.");
                }
            } else {
                System.out.println("Selección inválida.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Operación cancelada.");
            scanner.nextLine();
        }
    }

    /**
     * Asigna un producto a un slot de inventario, actualizando si ya existe (upsert).
     */
    public void asignarProductoASlot(ObjectId productoId, String codigoSlot, int stock) {

        Bson filtro = Filters.and(
                eq("maquinaId", maquinaActivaId),
                eq("codigoSlot", codigoSlot)
        );

        Document itemInventario = Mapper.inventarioToDocument(maquinaActivaId, productoId, codigoSlot, stock);
        Bson actualizacion = new Document("$set", itemInventario);

        UpdateResult resultado = inventarioCollection.updateOne(filtro, actualizacion, new UpdateOptions().upsert(true));

        if (resultado.getUpsertedId() != null) {
            System.out.println("-> Slot " + codigoSlot + " creado y asignado con éxito.");
        } else if (resultado.getModifiedCount() > 0) {
            System.out.println("-> Slot " + codigoSlot + " actualizado con éxito.");
        }
    }

    // -----------------------------------------------------
    // CONSULTAS / REPORTES
    // -----------------------------------------------------

    public void filtrarProductosPorPrecio() {
        System.out.println("\n--- FILTRAR PRODUCTOS POR PRECIO ---");
        System.out.print("Ingresa el precio mínimo: ");
        double precioMinimo = 0.0;
        try {
            precioMinimo = scanner.nextDouble();
            scanner.nextLine(); // Limpiar buffer
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Operación cancelada.");
            scanner.nextLine();
            return;
        }

        // Filtro: precio > precioMinimo
        Bson filtro = gt("precio", precioMinimo);

        List<Producto> productosFiltrados = new ArrayList<>();
        productosCollection.find(filtro).forEach(doc -> productosFiltrados.add(Mapper.documentToProducto(doc)));

        if (productosFiltrados.isEmpty()) {
            System.out.println("No se encontraron productos con precio mayor a $" + precioMinimo);
        } else {
            System.out.println("\nProductos con precio > $" + precioMinimo + ":");
            productosFiltrados.forEach(p -> System.out.println("- " + p.getNombre() + " ($" + p.getPrecio() + ")"));
        }
    }


    // -----------------------------------------------------
    // MÉTODOS DE OPERACIÓN
    // -----------------------------------------------------

    public Optional<Usuario> validarCredenciales(String user, String pass) {
        Bson filtro = Filters.and(
                eq("usuario", user),
                eq("passwordHash", pass)
        );
        Document userDoc = usuariosCollection.find(filtro).first();
        if (userDoc != null) {
            return Optional.of(Mapper.documentToUsuario(userDoc));
        }
        return Optional.empty();
    }

    public List<Producto> obtenerProductosDisponibles() {
        List<Producto> productos = new ArrayList<>();

        Bson filtroInventario = Filters.and(
                eq("maquinaId", maquinaActivaId),
                gt("stock", 0)
        );

        inventarioCollection.find(filtroInventario).forEach(itemInventario -> {
            ObjectId productoId = itemInventario.getObjectId("productoId");
            int stockDisponible = itemInventario.getInteger("stock", 0);
            String codigoSlot = itemInventario.getString("codigoSlot");

            Document productoDoc = productosCollection.find(eq("_id", productoId)).first();

            if (productoDoc != null) {
                Producto p = Mapper.documentToProducto(productoDoc);
                System.out.println(codigoSlot + " - " + p.getNombre() + " ($" + p.getPrecio() + ") [Stock: " + stockDisponible + "]");
                productos.add(p);
            }
        });
        return productos;
    }

    // -----------------------------------------------------
    // MENÚS
    // -----------------------------------------------------

    public void ejecutarMenu() {
        int opcion = -1;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Ver productos disponibles (Modo Usuario)");
            System.out.println("2. Acceso Técnico/Administrador");
            System.out.println("3. Salir");
            System.out.print("Elige una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Entrada inválida. Por favor, ingresa un número.");
                scanner.nextLine();
                opcion = 0;
            }

            switch (opcion) {
                case 1 -> modoUsuario();
                case 2 -> modoTecnico();
                case 3 -> System.out.println("Saliendo del programa...");
                default -> {
                    if (opcion != 0) {
                        System.out.println("Opción inválida.");
                    }
                }
            }
        } while (opcion != 3);
    }

    public void modoUsuario() {
        System.out.println("\n--- PRODUCTOS DISPONIBLES EN LA MÁQUINA ---");
        List<Producto> disponibles = obtenerProductosDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("Lo sentimos, la máquina está vacía o inoperativa.");
            return;
        }
        System.out.println("(Lógica de Venta pendiente de implementación...)");
    }

    public void modoTecnico() {
        System.out.println("\n--- ACCESO CON CREDENCIALES ---");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        Optional<Usuario> oUsuario = validarCredenciales(user, pass);

        if (oUsuario.isPresent()) {
            Usuario u = oUsuario.get();
            System.out.println("Acceso concedido. Rol: " + u.getRol());
            this.tecnicoLogueado = u.getUsuario();

            if (u.getRol().equals("admin")) {
                ejecutarMenuAdministrador();
            } else if (u.getRol().equals("tecnico")) {
                ejecutarMenuTecnico();
            } else {
                System.out.println("Rol no reconocido o acceso denegado.");
            }
            this.tecnicoLogueado = null;
        } else {
            System.out.println("Acceso denegado. Credenciales incorrectas.");
        }
    }

    // Menú de gestión para el rol 'admin'
    public void ejecutarMenuAdministrador() {
        int opcion = -1;
        do {
            System.out.println("\n--- MENÚ ADMINISTRADOR (" + tecnicoLogueado + ") ---");
            System.out.println("1. Registrar nueva Máquina");
            System.out.println("2. Seleccionar Máquina Activa");
            System.out.println("3. Eliminar Máquina");
            System.out.println("4. Registrar nuevo Producto (Catálogo)");
            System.out.println("5. Eliminar Producto (Catálogo)");
            System.out.println("6. Filtrar Productos por Precio Mínimo"); // Funcionalidad de filtrado
            System.out.println("7. Ir a Menú de Operaciones Técnicas");
            System.out.println("8. Volver al Menú Principal (Cerrar Sesión)");
            System.out.print("Elige una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Entrada inválida. Por favor, ingresa un número.");
                scanner.nextLine();
                opcion = 0;
            }

            switch (opcion) {
                case 1 -> registrarMaquina();
                case 2 -> seleccionarMaquina();
                case 3 -> eliminarMaquina();
                case 4 -> registrarProducto();
                case 5 -> eliminarProducto();
                case 6 -> filtrarProductosPorPrecio();
                case 7 -> ejecutarMenuTecnico();
                case 8 -> System.out.println("Cerrando sesión de administrador...");
                default -> {
                    if (opcion != 0) {
                        System.out.println("Opción inválida.");
                    }
                }
            }
        } while (opcion != 8);
    }

    // Menú de operaciones para el rol 'tecnico' (o accesible desde el admin)
    public void ejecutarMenuTecnico() {
        int opcion = -1;
        do {
            System.out.println("\n--- MENÚ OPERACIONES TÉCNICAS (" + tecnicoLogueado + ") ---");
            System.out.println("Máquina activa: " + (maquinaActivaId != null ? maquinaActivaId.toHexString() : "N/A"));
            System.out.println("1. Asignar/Actualizar Producto a Slot (Inventario)");
            System.out.println("2. Reponer Producto (Aumentar Stock - PENDIENTE)");
            System.out.println("3. Ver Reporte de Ventas (PENDIENTE)");
            System.out.println("4. Volver al menú anterior (Cerrar Sesión Técnica)");
            System.out.print("Elige una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Entrada inválida. Por favor, ingresa un número.");
                scanner.nextLine();
                opcion = 0;
            }

            switch (opcion) {
                case 1 -> {
                    if (maquinaActivaId == null) {
                        System.out.println("ERROR: Primero debes seleccionar o registrar una máquina activa.");
                        break;
                    }
                    // Lógica para Asignar Producto
                    List<Document> productosCatalogo = new ArrayList<>();
                    productosCollection.find().forEach(productosCatalogo::add);

                    if (productosCatalogo.isEmpty()) {
                        System.out.println("No hay productos en el catálogo para asignar. Regístralos en el menú de administrador.");
                        break;
                    }

                    System.out.println("\n--- PRODUCTOS DISPONIBLES (Catálogo General) ---");
                    for (int i = 0; i < productosCatalogo.size(); i++) {
                        Document pDoc = productosCatalogo.get(i);
                        System.out.println((i + 1) + ". " + pDoc.getString("nombre") +
                                " (ID: " + pDoc.getObjectId("_id").toHexString() + ")");
                    }

                    System.out.print("Selecciona el número del producto a asignar: ");
                    try {
                        int numProducto = scanner.nextInt();
                        scanner.nextLine();

                        if (numProducto < 1 || numProducto > productosCatalogo.size()) {
                            System.out.println("Selección inválida.");
                            break;
                        }

                        ObjectId productoId = productosCatalogo.get(numProducto - 1).getObjectId("_id");

                        System.out.print("Ingresa el código del slot (ej. A1, B4) donde se ubicará este producto: ");
                        String codigoSlot = scanner.nextLine().toUpperCase();

                        System.out.print("Ingresa el stock inicial para este slot: ");
                        int stock = scanner.nextInt();
                        scanner.nextLine();

                        asignarProductoASlot(productoId, codigoSlot, stock);
                    } catch (InputMismatchException e) {
                        System.out.println("Entrada inválida. Operación cancelada.");
                        scanner.nextLine();
                    }
                }
                case 2 -> System.out.println("Lógica de Reposición...");
                case 3 -> System.out.println("Lógica de Reporte de Ventas...");
                case 4 -> System.out.println("Volviendo al menú anterior...");
                default -> {
                    if (opcion != 0) {
                        System.out.println("Opción inválida.");
                    }
                }
            }
        } while (opcion != 4);
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión a MongoDB cerrada.");
        }
        if (scanner != null) {
            scanner.close();
        }
    }

    public static void main(String[] args) {
        MaquinaMongoDB app = null;
        try {
            app = new MaquinaMongoDB();
            app.ejecutarMenu();
        } catch (Exception e) {
            System.err.println("Un error fatal ocurrió en el Main.");
            e.printStackTrace();
        } finally {
            if (app != null) {
                app.close();
            }
        }
    }
}