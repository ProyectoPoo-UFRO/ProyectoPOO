import React from "react";
import styles from "./ProductCard.module.css";

export default function ProductCard({ product, onBuy }) {
    return (
        <div className={styles.card}>
            <div className={styles.imagePlaceholder}>
                🥤
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
                // Sobrescribimos el color para diferenciar "Agregar" de "Comprar"
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