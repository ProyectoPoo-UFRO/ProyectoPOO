import styles from "./ProductCard.module.css";
import { useUser } from "../../context/UserContext";

export default function ProductCard({ product, onBuy }) {
    const { user, toggleProductFavorite } = useUser();
    const isFavorite = user?.idLatasFavoritas?.includes(product.id);

    return (
        <div className={styles.card}>
            <button
                className={`${styles.favBtn} ${isFavorite ? styles.favBtnActive : ''}`}
                onClick={(e) => { e.stopPropagation(); toggleProductFavorite(product.id); }}
            >
                {isFavorite ? "❤️" : "🤍"}
            </button>

            <div className={styles.imageContainer}>
                <img src={product.image} alt={product.name} className={styles.image} />
            </div>

            <div className={styles.info}>
                <h3 className={styles.name}>{product.name}</h3>
                <div className={styles.priceRow}>
                    <span className={styles.price}>${product.price}</span>
                    <span className={styles.stock}>Stock: {product.stock}</span>
                </div>
            </div>

            <button
                className={styles.buyBtn}
                onClick={() => onBuy(product)}
                disabled={product.stock === 0}
            >
                {product.stock > 0 ? "Comprar" : "Agotado"}
            </button>
        </div>
    );
}