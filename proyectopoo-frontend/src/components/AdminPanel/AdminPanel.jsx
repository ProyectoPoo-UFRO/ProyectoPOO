import { useContext } from "react";
import { VendingContext } from "../../context/VendingContext";
import "./AdminPanel.css";

export default function AdminPanel() {
    const { products } = useContext(VendingContext);

    return (
        <div className="admin-panel">
            <h2>Panel Administrador</h2>

            <ul>
                {products.map(p => (
                    <li key={p.id}>
                        {p.name} — Stock: {p.stock}
                    </li>
                ))}
            </ul>
        </div>
    );
}
