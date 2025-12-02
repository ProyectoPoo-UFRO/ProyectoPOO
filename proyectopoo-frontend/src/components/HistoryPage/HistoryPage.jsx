import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import styles from "./HistoryPage.module.css"; // Asegúrate de haber creado este CSS

export default function HistoryPage() {
    const { history } = useUser();
    const navigate = useNavigate();

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h1 className={styles.title}>📜 Mis Compras</h1>
                <button onClick={() => navigate("/home")} className={styles.backButton}>
                    ← Volver
                </button>
            </div>

            {history.length === 0 ? (
                <div className={styles.emptyState}>
                    <p>Aún no has realizado ninguna compra.</p>
                </div>
            ) : (
                <div className={styles.tableContainer}>
                    <table className={styles.table}>
                        <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Máquina</th>
                            <th>Detalle Productos</th>
                            <th>Total</th>
                        </tr>
                        </thead>
                        <tbody>
                        {history.map((record) => (
                            <tr key={record.id}>
                                <td>{record.date}</td>
                                <td>{record.machine}</td>
                                <td>
                                    <ul className={styles.itemList}>
                                        {record.items.map((item, index) => (
                                            <li key={index}>
                                                {item.quantity}x {item.name} (${item.price})
                                            </li>
                                        ))}
                                    </ul>
                                </td>
                                <td className={styles.totalColumn}>
                                    ${record.total}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}