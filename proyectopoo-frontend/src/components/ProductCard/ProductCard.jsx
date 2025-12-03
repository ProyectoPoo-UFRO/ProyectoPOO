import React from "react";
import styles from "./ProductCard.module.css";

export default function ProductCard({ product, onBuy }) {

    const handleImageError = (e) => {
        e.target.src = "https://via.placeholder.com/150?text=Sin+Imagen";
        e.target.onerror = null;
    };

    return (
        <div className={styles.card}>
            <div className={styles.imageContainer}>
                <img
                    src={product.image || "https://via.placeholder.com/150?text=Producto"}
                    alt={product.name}
                    className={styles.productImage}
                    onError={handleImageError}
                />
            </div>

            <div className={styles.info}>
                <div className={styles.name}>{product.name}</div>
                <div className={styles.stock}>Stock: {product.stock}</div>
                <div className={styles.price}>${product.price}</div>
            </div>

            <button
                onClick={() => onBuy(product)}
                disabled={product.stock === 0}
                className={styles.button}
                style={{ backgroundColor: product.stock > 0 ? '#f39c12' : '#555' }}
            >
                {product.stock > 0 ? "Agregar al Carrito" : "Agotado"}
            </button>

            {product.stock === 0 && (
                <div className={styles.outOfStockOverlay}>
                    AGOTADO
                </div>
            )}
        </div>
    );
}