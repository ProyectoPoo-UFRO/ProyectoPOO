package org.example;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.ZoneId;
import java.util.Date;


public class Mapper {


    public static Document productoToDocument(Producto producto) {
        Document doc = new Document()
                .append("nombre", producto.getNombre())
                .append("precio", producto.getPrecio())
                .append("categoria", producto.getCategoria())
                .append("imagenUrl", producto.getImagenUrl());
        if (producto.getId() != null) {
            doc.append("_id", producto.getId());
        }
        return doc;
    }

    public static Producto documentToProducto(Document doc) {
        Producto producto = new Producto();
        producto.setId(doc.getObjectId("_id"));
        producto.setNombre(doc.getString("nombre"));
        // Manejo seguro de nulos para Double (precio)
        Double precio = doc.getDouble("precio");
        producto.setPrecio(precio != null ? precio : 0.0);
        producto.setCategoria(doc.getString("categoria"));
        producto.setImagenUrl(doc.getString("imagenUrl"));
        return producto;
    }


    public static Document ventaToDocument(Venta venta) {
        Document doc = new Document()
                .append("productoId", venta.getProductoId())
                .append("cantidad", venta.getCantidad())
                .append("montoPagado", venta.getMontoPagado())
                .append("vueltoEntregado", venta.getVueltoEntregado())
                .append("fecha", Date.from(venta.getFecha().atZone(ZoneId.systemDefault()).toInstant()));
        if (venta.getId() != null) {
            doc.append("_id", venta.getId());
        }
        return doc;
    }

    public static Venta documentToVenta(Document doc) {
        Venta venta = new Venta();
        venta.setId(doc.getObjectId("_id"));
        venta.setProductoId(doc.getObjectId("productoId"));
        venta.setCantidad(doc.getInteger("cantidad"));
        // Manejo seguro de nulos para Double (montoPagado, vueltoEntregado)
        Double montoPagado = doc.getDouble("montoPagado");
        venta.setMontoPagado(montoPagado != null ? montoPagado : 0.0);
        Double vueltoEntregado = doc.getDouble("vueltoEntregado");
        venta.setVueltoEntregado(vueltoEntregado != null ? vueltoEntregado : 0.0);
        venta.setFecha(doc.getDate("fecha").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return venta;
    }


    public static Document reposicionToDocument(Reposicion reposicion) {
        Document doc = new Document()
                .append("productoId", reposicion.getProductoId())
                .append("cantidadAgregada", reposicion.getCantidadAgregada())
                .append("tecnico", reposicion.getTecnico())
                .append("fecha", Date.from(reposicion.getFecha().atZone(ZoneId.systemDefault()).toInstant()));
        if (reposicion.getId() != null) {
            doc.append("_id", reposicion.getId());
        }
        return doc;
    }

    public static Reposicion documentToReposicion(Document doc) {
        Reposicion reposicion = new Reposicion();
        reposicion.setId(doc.getObjectId("_id"));
        reposicion.setProductoId(doc.getObjectId("productoId"));
        reposicion.setCantidadAgregada(doc.getInteger("cantidadAgregada"));
        reposicion.setTecnico(doc.getString("tecnico"));
        reposicion.setFecha(doc.getDate("fecha").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return reposicion;
    }


    public static Document usuarioToDocument(Usuario usuario) {
        Document doc = new Document()
                .append("usuario", usuario.getUsuario())
                .append("passwordHash", usuario.getPasswordHash())
                .append("rol", usuario.getRol());
        if (usuario.getId() != null) {
            doc.append("_id", usuario.getId());
        }
        return doc;
    }

    public static Usuario documentToUsuario(Document doc) {
        Usuario usuario = new Usuario();
        usuario.setId(doc.getObjectId("_id"));
        usuario.setUsuario(doc.getString("usuario"));
        usuario.setPasswordHash(doc.getString("passwordHash"));
        usuario.setRol(doc.getString("rol"));
        return usuario;
    }


    public static Document maquinaToDocument(MaquinaExpendedora maquina) {
        Document doc = new Document()
                .append("codigo", maquina.getCodigo())
                .append("latitud", maquina.getLatitud())
                .append("longitud", maquina.getLongitud())
                .append("ubicacion", maquina.getUbicacion())
                .append("estado", maquina.getEstado());
        if (maquina.getId() != null) {
            doc.append("_id", maquina.getId());
        }
        return doc;
    }

    public static MaquinaExpendedora documentToMaquina(Document doc) {
        if (doc == null) return null;

        MaquinaExpendedora maquina = new MaquinaExpendedora();
        maquina.setId(doc.getObjectId("_id"));
        maquina.setCodigo(doc.getString("codigo"));

        Double latitud = doc.getDouble("latitud");
        maquina.setLatitud(latitud != null ? latitud : 0.0);
        Double longitud = doc.getDouble("longitud");
        maquina.setLongitud(longitud != null ? longitud : 0.0);

        maquina.setUbicacion(doc.getString("ubicacion"));
        maquina.setEstado(doc.getString("estado"));
        return maquina;
    }

    public static Document inventarioToDocument(ObjectId maquinaId, ObjectId productoId, String codigoSlot, int stock) {
        return new Document("maquinaId", maquinaId)
                .append("productoId", productoId)
                .append("codigoSlot", codigoSlot)
                .append("stock", stock);
    }
}