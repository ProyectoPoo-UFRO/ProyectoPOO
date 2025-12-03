import { useParams, useNavigate } from "react-router-dom";
import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useCart } from "../../context/CartContext";
import { useState, useEffect } from "react";
import ProductCard from "../ProductCard/ProductCard";
import Spinner from "../Spinner/Spinner";
import styles from "./VendingMachine.module.css";

export default function VendingMachine() {
    const { id } = useParams(); // Obtenemos el ID de la URL (es un String)
    const navigate = useNavigate();

    const { machines, decreaseStock, loading } = useVending();
    const { user, deductBalance, addPurchase } = useUser();
    const { cart, addToCart, removeFromCart, clearCart } = useCart();

    const [message, setMessage] = useState("");
    const [searchTerm, setSearchTerm] = useState("");

    // --- CORRECCIÓN CRÍTICA ---
    // Buscamos la máquina comparando Strings. NO usamos Number(id) porque los IDs de Mongo son texto.
    const currentMachine = machines.find(m => String(m.id) === String(id));

    // Redirección segura
    useEffect(() => {
        // Solo redirigimos si ya terminó de cargar y aun así no encontró la máquina
        if (!loading && !currentMachine) {
            console.warn("Máquina no encontrada, redirigiendo...");
            navigate("/home");
        }
    }, [currentMachine, loading, navigate]);

    // 1. Mostrar Spinner mientras carga
    if (loading) return <Spinner />;

    // 2. Si no hay máquina, no renderizar nada (el useEffect redirigirá)
    if (!currentMachine) return null;

    // --- LÓGICA DEL COMPONENTE ---

    const filteredProducts = currentMachine.products.filter(product =>
        product.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

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

    const handleCheckout = () => {
        if (cart.length === 0) {
            setMessage("❌ El carrito está vacío");
            return;
        }

        const total = cart.reduce((acc, item) => acc + (item.price * item.quantity), 0);

        if (user.balance < total) {
            setMessage("❌ Saldo insuficiente");
            return;
        }

        // Verificación final de stock antes de comprar
        for (const item of cart) {
            const productReal = currentMachine.products.find(p => p.id === item.id);
            if (!productReal || productReal.stock < item.quantity) {
                setMessage(`❌ Error de stock con ${item.name}`);
                return;
            }
        }

        // Procesar compra
        deductBalance(total);
        cart.forEach(item => {
            decreaseStock(currentMachine.id, item.id, item.quantity);
        });
        addPurchase(cart, total, currentMachine.name);

        clearCart();
        setMessage(`✔ ¡Compra exitosa! Total: $${total}`);
        setSearchTerm("");
    };

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <div>
                    <h1 className={styles.machineTitle}>{currentMachine.name}</h1>
                    <p className={styles.machineLocation}>{currentMachine.location}</p>
                </div>
                <button onClick={() => navigate("/home")} className={styles.backButton}>← Volver</button>
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
                    </div>

                    <div className={styles.productsGrid}>
                        {filteredProducts.length > 0 ? (
                            filteredProducts.map((p) => (
                                <ProductCard
                                    key={p.id}
                                    product={p}
                                    onBuy={handleAddToCart}
                                />
                            ))
                        ) : (
                            <p style={{ color: '#94a3b8', gridColumn: '1 / -1', textAlign: 'center', padding: '20px' }}>
                                No se encontraron productos.
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
                        <div
                            className={styles.messageBox}
                            style={{
                                backgroundColor: message.includes('❌') ? 'rgba(239, 68, 68, 0.1)' : 'rgba(16, 185, 129, 0.1)',
                                color: message.includes('❌') ? '#fca5a5' : '#6ee7b7',
                                border: message.includes('❌') ? '1px solid rgba(239, 68, 68, 0.2)' : '1px solid rgba(16, 185, 129, 0.2)'
                            }}
                        >
                            {message}
                        </div>
                    )}

                    <div className={styles.cartContainer}>
                        <h3 className={styles.cartTitle}>🛒 Carrito</h3>
                        {cart.length === 0 ? (
                            <p style={{ color: '#64748b', textAlign: 'center', padding: '20px 0', flexGrow: 1 }}>
                                Carrito vacío...
                            </p>
                        ) : (
                            <>
                                <ul className={styles.cartList}>
                                    {cart.map(item => (
                                        <li key={item.id} className={styles.cartItem}>
                                            <span>{item.quantity}x {item.name}</span>
                                            <button
                                                onClick={() => removeFromCart(item.id)}
                                                className={styles.removeItemBtn}
                                            >
                                                ×
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                                <div className={styles.cartTotal}>
                                    <span>Total:</span>
                                    <span className={styles.totalPrice}>
                                        ${cart.reduce((acc, item) => acc + (item.price * item.quantity), 0)}
                                    </span>
                                </div>
                                <button onClick={handleCheckout} className={styles.checkoutBtn}>
                                    Pagar Ahora
                                </button>
                                <button onClick={clearCart} className={styles.clearBtn}>
                                    Vaciar carrito
                                </button>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}