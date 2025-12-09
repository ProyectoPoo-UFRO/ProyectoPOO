import { useParams, useNavigate } from "react-router-dom";
import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useCart } from "../../context/CartContext";
import { useState, useEffect } from "react";
import ProductCard from "../ProductCard/ProductCard";
import Spinner from "../Spinner/Spinner";
import styles from "./VendingMachine.module.css";
import { registerSale } from "../../services/machineService";

export default function VendingMachine() {
    const { id } = useParams();
    const navigate = useNavigate();

    const { machines, decreaseStock, loading } = useVending();
    const { user, deductBalance, addPurchase, logout } = useUser();
    const { cart, addToCart, removeFromCart, clearCart } = useCart();

    const [message, setMessage] = useState("");
    const [searchTerm, setSearchTerm] = useState("");
    const [showFavoritesOnly, setShowFavoritesOnly] = useState(false);

    const currentMachine = machines.find(m => String(m.id) === String(id));

    useEffect(() => {
        if (!loading && !currentMachine) {
            navigate("/home");
        }
    }, [currentMachine, loading, navigate]);

    if (loading) return <Spinner />;
    if (!currentMachine) return null;

    const filteredProducts = currentMachine.products.filter(product => {
        const matchesSearch = product.name.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesFav = showFavoritesOnly ? user?.idLatasFavoritas?.includes(product.id) : true;
        return matchesSearch && matchesFav;
    });

    const handleAddToCart = (product) => {
        const itemInCart = cart.find(item => item.id === product.id);
        const quantityInCart = itemInCart ? itemInCart.quantity : 0;

        if (quantityInCart + 1 > product.stock) {
            setMessage(`❌ Stock insuficiente de ${product.name}`);
            return;
        }
        addToCart(product);
        setMessage(`🛒 ${product.name} agregado`);
    };

    const handleCheckout = async () => {
        if (cart.length === 0) { setMessage("❌ El carrito está vacío"); return; }
        const total = cart.reduce((acc, item) => acc + (item.price * item.quantity), 0);
        if (user.balance < total) { setMessage("❌ Saldo insuficiente"); return; }

        const ventaPayload = {
            idMaquina: currentMachine.id,
            idUsuario: user.id,
            items: cart.map(item => ({ lataId: item.id, cantidad: item.quantity }))
        };

        try {
            const response = await registerSale(ventaPayload);
            if (!response) { setMessage("❌ Error al procesar venta"); return; }

            deductBalance(total);
            cart.forEach(item => decreaseStock(currentMachine.id, item.id, item.quantity));
            addPurchase(cart, total, currentMachine.name);
            clearCart();
            setMessage(`✔ ¡Compra exitosa! Total: $${total}`);
            setSearchTerm("");
        } catch (error) {
            console.error("Error en checkout:", error);
            setMessage("❌ Error de conexión al comprar");
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <div className={styles.headerLeft}>
                    <button onClick={() => navigate("/home")} className={styles.backButton}>← Volver</button>
                </div>
                <div className={styles.headerCenter}>
                    <h2>Hola, <strong className={styles.userNameHighlight}>{user?.name}</strong></h2>
                    <span>Selecciona una máquina</span>
                </div>
                <div className={styles.headerActions}>
                    <button onClick={() => navigate("/history")} className={styles.historyButton}>📜 Mis Compras</button>
                    <button onClick={() => { logout(); navigate("/"); }} className={styles.logoutButton}>Cerrar sesión</button>
                </div>
            </div>

            <div className={styles.mainLayout}>
                <div className={styles.productsArea}>
                    <div className={styles.searchContainer}>
                        <span className={styles.searchLabel}>Buscador</span>
                        <input
                            type="text"
                            className={styles.searchInput}
                            placeholder="Buscar productos..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />

                        <button
                            className={`${styles.filterFavBtn} ${showFavoritesOnly ? styles.active : ''}`}
                            onClick={() => setShowFavoritesOnly(!showFavoritesOnly)}
                            title="Ver solo mis favoritos"
                        >
                            {showFavoritesOnly ? "❤️ Favoritos" : "🤍 Favoritos"}
                        </button>
                    </div>

                    <div className={styles.productsGrid}>
                        {filteredProducts.length > 0 ? (
                            filteredProducts.map((p) => (
                                <ProductCard key={p.id} product={p} onBuy={handleAddToCart} />
                            ))
                        ) : (
                            <p style={{ color: '#94a3b8', gridColumn: '1 / -1', textAlign: 'center', padding: '20px' }}>
                                {showFavoritesOnly ? "No tienes favoritos en esta máquina." : "No se encontraron productos."}
                            </p>
                        )}
                    </div>
                </div>

                <div className={styles.sidebar}>
                    <div className={styles.balancePanel}>
                        <p className={styles.balanceLabel}>Saldo Disponible</p>
                        <h2 className={styles.balanceAmount}>${user.balance}</h2>
                    </div>
                    {message && (
                        <div className={styles.messageBox} style={{
                            backgroundColor: message.includes('❌') ? 'rgba(239, 68, 68, 0.1)' : 'rgba(16, 185, 129, 0.1)',
                            color: message.includes('❌') ? '#fca5a5' : '#6ee7b7',
                            border: message.includes('❌') ? '1px solid rgba(239, 68, 68, 0.2)' : '1px solid rgba(16, 185, 129, 0.2)'
                        }}>{message}</div>
                    )}
                    <div className={styles.cartContainer}>
                        <h3 className={styles.cartTitle}>🛒 Carrito</h3>
                        {cart.length === 0 ? (
                            <p style={{ color: '#64748b', textAlign: 'center', padding: '20px 0', flexGrow: 1 }}>Carrito vacío...</p>
                        ) : (
                            <>
                                <ul className={styles.cartList}>
                                    {cart.map(item => (
                                        <li key={item.id} className={styles.cartItem}>
                                            <span>{item.quantity}x {item.name}</span>
                                            <button onClick={() => removeFromCart(item.id)} className={styles.removeItemBtn}>×</button>
                                        </li>
                                    ))}
                                </ul>
                                <div className={styles.cartTotal}>
                                    <span>Total:</span>
                                    <span className={styles.totalPrice}>${cart.reduce((acc, item) => acc + (item.price * item.quantity), 0)}</span>
                                </div>
                                <button onClick={handleCheckout} className={styles.checkoutBtn}>Pagar Ahora</button>
                                <button onClick={clearCart} className={styles.clearBtn}>Vaciar carrito</button>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}