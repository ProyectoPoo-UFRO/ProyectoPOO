import { useParams, useNavigate } from "react-router-dom";
import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useCart } from "../../context/CartContext";
import { useState, useEffect } from "react";
import ProductCard from "../ProductCard/ProductCard";
import styles from "./VendingMachine.module.css";

export default function VendingMachine() {
    const { id } = useParams();
    const navigate = useNavigate();

    // decreaseStock ahora acepta cantidad (qty)
    const { machines, decreaseStock } = useVending();
    const { user, deductBalance } = useUser();
    const { cart, addToCart, removeFromCart, clearCart } = useCart();

    const [message, setMessage] = useState("");

    const currentMachine = machines.find(m => m.id === Number(id));

    useEffect(() => {
        if (!currentMachine) navigate("/home");
    }, [currentMachine, navigate]);

    if (!currentMachine) return <p style={{color: 'white', padding: 20}}>Cargando...</p>;

    // --- NUEVA LÓGICA: AGREGAR AL CARRITO ---
    const handleAddToCart = (product) => {
        // Verificamos cuánto de este producto ya tengo en el carrito
        const itemInCart = cart.find(item => item.id === product.id);
        const quantityInCart = itemInCart ? itemInCart.quantity : 0;

        // Validamos que no agregue más del stock real disponible
        if (quantityInCart + 1 > product.stock) {
            setMessage(`❌ No hay suficiente stock de ${product.name}`);
            return;
        }

        addToCart(product);
        setMessage(`🛒 ${product.name} agregado al carrito`);
    };

    // --- NUEVA LÓGICA: CHECKOUT (COMPRA FINAL) ---
    const handleCheckout = () => {
        if (cart.length === 0) {
            setMessage("❌ El carrito está vacío");
            return;
        }

        // Calcular total
        const total = cart.reduce((acc, item) => acc + (item.price * item.quantity), 0);

        // 1. Validar Saldo
        if (user.balance < total) {
            setMessage("❌ Saldo insuficiente para realizar la compra");
            return;
        }

        // 2. Validar Stock Final (Seguridad)
        for (const item of cart) {
            const productReal = currentMachine.products.find(p => p.id === item.id);
            if (productReal.stock < item.quantity) {
                setMessage(`❌ Stock insuficiente para ${item.name} (Quedan: ${productReal.stock})`);
                return;
            }
        }

        // 3. EJECUTAR COMPRA
        deductBalance(total); // Descontar dinero

        // Descontar stock de cada producto según cantidad
        cart.forEach(item => {
            decreaseStock(currentMachine.id, item.id, item.quantity);
        });

        clearCart();
        setMessage(`✔ ¡Compra realizada con éxito! Total: $${total}`);
    };

    return (
        <div className={styles.container}>
            {/* HEADER */}
            <div className={styles.header}>
                <div>
                    <h1 className={styles.machineTitle}>{currentMachine.name}</h1>
                    <p className={styles.machineLocation}>{currentMachine.location}</p>
                </div>
                <button onClick={() => navigate("/home")} className={styles.backButton}>← Volver</button>
            </div>

            <div className={styles.mainLayout}>

                {/* IZQUIERDA: LISTA DE PRODUCTOS */}
                <div className={styles.productsArea}>
                    <div className={styles.productsGrid}>
                        {currentMachine.products.map((p) => (
                            <ProductCard
                                key={p.id}
                                product={p}
                                onBuy={handleAddToCart} // Acción: Agregar al carrito
                            />
                        ))}
                    </div>
                </div>

                {/* DERECHA: SIDEBAR / CONTROLES */}
                <div className={styles.sidebar}>

                    {/* Panel de Saldo */}
                    <div style={{ padding: '15px', background: '#333', borderRadius: '5px', textAlign: 'center', border: '1px solid #444' }}>
                        <p style={{ margin: 0, color: '#aaa', fontSize: '0.8rem', textTransform: 'uppercase' }}>Saldo Disponible</p>
                        <h2 style={{ margin: '5px 0', color: '#fbc531' }}>${user.balance}</h2>
                    </div>

                    {/* Mensajes del Sistema */}
                    {message && (
                        <div style={{
                            padding: '10px',
                            background: message.includes('❌') ? 'rgba(232, 65, 24, 0.2)' : 'rgba(76, 209, 55, 0.2)',
                            color: message.includes('❌') ? '#ff6b6b' : '#4cd137',
                            borderRadius: '4px',
                            textAlign: 'center',
                            fontSize: '0.9rem',
                            border: message.includes('❌') ? '1px solid #ff6b6b' : '1px solid #4cd137'
                        }}>
                            {message}
                        </div>
                    )}

                    {/* Carrito */}
                    <div className={styles.cartContainer}>
                        <h3 className={styles.cartTitle}>🛒 Carrito</h3>
                        {cart.length === 0 ? (
                            <p style={{ color: '#777', fontStyle: 'italic', textAlign: 'center', padding: '20px 0' }}>Vacío...</p>
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
                                    Total: ${cart.reduce((acc, item) => acc + (item.price * item.quantity), 0)}
                                </div>

                                {/* BOTÓN FINAL DE COMPRA */}
                                <button
                                    onClick={handleCheckout}
                                    className={styles.clearButton}
                                    style={{ backgroundColor: '#27ae60', marginTop: '15px' }}
                                >
                                    ✅ Realizar Compra
                                </button>

                                <button
                                    onClick={clearCart}
                                    style={{ background: 'none', border: 'none', color: '#aaa', width: '100%', marginTop: '10px', cursor: 'pointer', fontSize: '0.8rem' }}
                                >
                                    Vaciar todo
                                </button>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}