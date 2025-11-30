package org.example;

import org.bson.Document;

import java.time.ZoneId;
import java.util.Date;

public class Mapper {

    private Mapper(){
    }

    public static Producto documentToProducto (Document doc){
        if (doc == null) return null;
        Producto producto = new Producto("Coca Cola", 700.00, "Bebida", "A1", 5, "cola.jpg");
        producto.setId(doc.getObjectId("_id"));
        producto.setNombre(doc.getString("nombre"));

        Object precioObjeto = doc.get("precio");
        if (precioObjeto instanceof Number) {
            producto.setPrecio(((Number) precioObjeto).doubleValue());
        } else {
            producto.setPrecio(0.0);
        }

        producto.setCategoria(doc.getString("categoria"));
        producto.setCodigo(doc.getString("codigoSlot"));

        Object stockObjetos = doc.get("stock");
        if (stockObjetos instanceof Number){
            producto.setStock(((Number) stockObjetos).intValue());
        } else {
            producto.setStock(0);
        }

        producto.setImagen(doc.getString("imagen"));
        return producto;
    }

    public static Document productoToDocument(Producto producto) {
        Document doc = new Document()
                .append("nombre", producto.getNombre())
                .append("precio", producto.getPrecio())
                .append("categoria", producto.getCategoria())
                .append("codigoSlot", producto.getCodigo())
                .append("stock", producto.getStock())
                .append("imagen", producto.getImagen());

        if (producto.getId() != null) {
            doc.append("_id", producto.getId());
        }
        return doc;
    }
    public static Document ventaToDocument(Venta venta) {
        // La fecha debe ser convertida a Date para que MongoDB la guarde correctamente
        Date date = Date.from(venta.getFecha().atZone(ZoneId.systemDefault()).toInstant());

        Document doc = new Document()
                .append("productoId", venta.getProductoId())
                .append("cantidad", venta.getCantidad())
                .append("montoPagado", venta.getMontoPagado())
                .append("vueltoEntregado", venta.getVueltoEntrado())
                .append("fecha", date);

        if (venta.getId() != null) {
            doc.append("_id", venta.getId());
        }
        return doc;
    }
    public static Document reposicionToDocument(Reposicion reposicion) {
        Date date = Date.from(reposicion.getFechaReposicion().atZone(ZoneId.systemDefault()).toInstant());

        Document doc = new Document()
                .append("productoId", reposicion.getProductoId())
                .append("cantidadAgregada", reposicion.getCantidadAgregada())
                .append("fecha", date)
                .append("tecnico", reposicion.getTecnico());

        if (reposicion.getId() != null) {
            doc.append("_id", reposicion.getId());
        }
        return doc;
    }
}
