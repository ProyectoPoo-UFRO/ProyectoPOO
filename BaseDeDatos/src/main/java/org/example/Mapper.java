package org.example;

import org.bson.Document;
import java.util.ArrayList;

public class Mapper {

    // -------------------------- Maquina → Document --------------------------
    public static Document maquinaToDocument(MaquinaExpendedora m) {

        Document d = new Document()
                .append("id", m.getId())
                .append("estado", m.getEstado())
                .append("ubicacion", coordenadasToDocument(m.getUbicacion()));

        ArrayList<Document> listaProductos = new ArrayList<>();
        for (Producto p : m.getProductos()) {
            listaProductos.add(productoToDocument(p));
        }

        d.append("productos", listaProductos);

        return d;
    }

    // -------------------------- Document → Maquina --------------------------
    public static MaquinaExpendedora documentToMaquina(Document d) {

        MaquinaExpendedora m = new MaquinaExpendedora();

        m.setId(d.getString("id"));
        m.setEstado(d.getString("estado"));
        m.setUbicacion(documentToCoordenadas((Document) d.get("ubicacion")));

        ArrayList<Document> prodDocs = (ArrayList<Document>) d.get("productos");

        if (prodDocs != null) {
            for (Document dp : prodDocs) {
                m.agregarProducto(documentToProducto(dp));
            }
        }

        return m;
    }

    // -------------------------- Producto → Document --------------------------
    public static Document productoToDocument(Producto p) {
        return new Document()
                .append("nombre", p.getNombre())
                .append("stock", p.getStock())
                .append("precio", p.getPrecio());
    }

    // -------------------------- Document → Producto --------------------------
    public static Producto documentToProducto(Document d) {

        Producto p = new Producto();

        p.setNombre(d.getString("nombre"));
        p.setStock(d.getInteger("stock"));
        p.setPrecio(d.getInteger("precio"));

        return p;
    }

    // -------------------------- Coordenadas → Document --------------------------
    public static Document coordenadasToDocument(Coordenadas c) {
        return new Document()
                .append("latitud", c.getLatitud())
                .append("longitud", c.getLongitud());
    }

    // -------------------------- Document → Coordenadas --------------------------
    public static Coordenadas documentToCoordenadas(Document d) {
        Coordenadas c = new Coordenadas();

        c.setLatitud(d.getDouble("latitud"));
        c.setLongitud(d.getDouble("longitud"));

        return c;
    }
}
