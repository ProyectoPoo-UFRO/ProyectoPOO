import React from "react";
import styles from "./ProductCard.module.css";

export default function ProductCard({ product, onBuy }) {

    // Función de seguridad: Si la imagen falla (link roto), pone una por defecto
    const handleImageError = (e) => {
        e.target.src = "https://via.placeholder.com/150?text=Sin+Imagen";
        e.target.onerror = null; // Previene bucles infinitos si la imagen de error también falla
    };

    return (
        <div className={styles.card}>
            {/* Contenedor de Imagen */}
            <div className={styles.imageContainer}>
                <img
                    src={product.image || "https://via.placeholder.com/150?text=Producto"}
                    alt={product.name}
                    className={styles.productImage}
                    onError={handleImageError} // <--- Protección contra links rotos
                />
            </div>

            {/* Información del Producto */}
            <div className={styles.info}>
                <div className={styles.name}>{product.name}</div>
                <div className={styles.stock}>Stock: {product.stock}</div>
                <div className={styles.price}>${product.price}</div>
            </div>

            {/* Botón de Acción */}
            <button
                onClick={() => onBuy(product)}
                disabled={product.stock === 0}
                className={styles.button}
                // Color Naranja (#f39c12) para "Agregar", Gris (#555) para "Agotado"
                style={{ backgroundColor: product.stock > 0 ? '#f39c12' : '#555' }}
            >
                {product.stock > 0 ? "Agregar al Carrito" : "Agotado"}
            </button>

            {/* Overlay visual cuando no hay stock */}
            {product.stock === 0 && (
                <div className={styles.outOfStockOverlay}>
                    AGOTADO
                </div>
            )}
        </div>
    );
}