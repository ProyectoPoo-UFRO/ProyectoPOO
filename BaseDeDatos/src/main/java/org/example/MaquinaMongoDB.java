package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MaquinaMongoDB {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> coleccion;

    public MaquinaMongoDB() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("vendingDB");
        coleccion = database.getCollection("maquinas");
    }

    public void insertarMaquina(MaquinaExpendedora m) {
        Document ubic = new Document("lat", m.getUbicacion().getLatitud())
                .append("lng", m.getUbicacion().getLongitud());

        Document doc = new Document("id", m.getId())
                .append("estado", m.getEstado())
                .append("ubicacion", ubic)
                .append("productos", new ArrayList<Document>());

        coleccion.insertOne(doc);
    }

    public List<Document> obtenerMaquinas() {
        return coleccion.find().into(new ArrayList<>());
    }
    public MaquinaExpendedora obtenerMaquina(String id) {
        Document doc = coleccion.find(Filters.eq("id", id)).first();
        if (doc == null) return null;

        Document ubic = (Document) doc.get("ubicacion");
        double lat = ubic != null && ubic.get("lat") != null ? ((Number) ubic.get("lat")).doubleValue() : 0.0;
        double lng = ubic != null && ubic.get("lng") != null ? ((Number) ubic.get("lng")).doubleValue() : 0.0;

        Coordenadas c = new Coordenadas(lat, lng);
        MaquinaExpendedora m = new MaquinaExpendedora(
                doc.getString("id"),
                doc.getString("estado"),
                c
        );

        List<Document> prods = (List<Document>) doc.get("productos");
        if (prods != null) {
            for (Document pdoc : prods) {
                String nombre = pdoc.getString("nombre");
                int stock = pdoc.getInteger("stock", 0);
                int precio = pdoc.getInteger("precio", 0);
                m.agregarProducto(new Producto(nombre, stock, precio));
            }
        }

        return m;
    }

    public void actualizarProductos(String id, ArrayList<Producto> productos) {
        List<Document> lista = new ArrayList<>();
        for (Producto p : productos) {
            lista.add(new Document("nombre", p.getNombre())
                    .append("stock", p.getStock())
                    .append("precio", p.getPrecio()));
        }

        coleccion.updateOne(
                Filters.eq("id", id),
                new Document("$set", new Document("productos", lista))
        );
    }

    public void actualizarEstado(String id, String nuevoEstado) {
        coleccion.updateOne(
                Filters.eq("id", id),
                new Document("$set", new Document("estado", nuevoEstado))
        );
    }

    public void eliminarMaquina(String id) {
        coleccion.deleteOne(Filters.eq("id", id));
    }

    public void close() {
        if (mongoClient != null) mongoClient.close();
    }
}
