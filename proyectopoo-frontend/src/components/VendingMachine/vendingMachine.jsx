import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useState } from "react";

export default function VendingMachine() {
    const { products, decreaseStock } = useVending();
    const { logout, user, deductBalance } = useUser();
    const [message, setMessage] = useState("");

    const buyProduct = (product) => {
        if (product.quantity <= 0) {
            setMessage(`❌ ${product.name} está agotado`);
            return;
        }

        if (user.balance < product.price) {
            setMessage(`❌ Saldo insuficiente para comprar ${product.name}`);
            return;
        }

        deductBalance(product.price);
        decreaseStock(product.id);
        setMessage(`✔ Compraste ${product.name}`);
    };

    return (
        <div style={{ padding: "20px" }}>
            <h1>Máquina Expendedora</h1>
            <p>Bienvenido: <strong>{user?.name}</strong></p>
            <p>💵 Saldo disponible: <strong>${user.balance}</strong></p>

            <button onClick={logout}>Cerrar sesión</button>

            <h2>Productos</h2>
            {message && <p><strong>{message}</strong></p>}

            <ul>
                {products.map((p) => (
                    <li key={p.id} style={{ marginBottom: "10px" }}>
                        {p.name} — ${p.price} — Stock: {p.quantity}

                        <button
                            onClick={() => buyProduct(p)}
                            disabled={p.quantity === 0}
                            style={{
                                marginLeft: "10px",
                                opacity: p.quantity === 0 ? 0.5 : 1,
                                cursor: p.quantity === 0 ? "not-allowed" : "pointer"
                            }}
                        >
                            {p.quantity > 0 ? "Comprar" : "Agotado"}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
