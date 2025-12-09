import { useEffect, useState } from "react";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import styles from "./HistoryPage.module.css";

const API_URL = "http://localhost:8081/api/v1";

export default function HistoryPage() {
    const { user } = useUser();
    const navigate = useNavigate();
    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(true);
    const [latasMap, setLatasMap] = useState({});

    useEffect(() => {
        if (!user) {
            navigate("/");
            return;
        }

        const fetchData = async () => {
            try {
                const cleanToken = user.token.toString().trim().replace(/^"|"$/g, '');
                const headers = {
                    'Authorization': `Bearer ${cleanToken}`,
                    'Content-Type': 'application/json'
                };

                const latasRes = await fetch(`${API_URL}/latas`, { headers });
                if (latasRes.ok) {
                    const latasData = await latasRes.json();
                    const mapa = {};
                    latasData.forEach(l => {
                        mapa[l.id] = l.nombre;
                        mapa[l._id] = l.nombre;
                    });
                    setLatasMap(mapa);
                }

                const historyRes = await fetch(`${API_URL}/ventas/usuario/${user.id}`, { headers });
                if (historyRes.ok) {
                    const historyData = await historyRes.json();
                    setHistory(historyData.reverse());
                } else {
                    console.warn("No se pudo cargar el historial o está vacío");
                }

            } catch (error) {
                console.error("Error cargando historial:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [user, navigate]);

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <button onClick={() => navigate("/home")} className={styles.backButton}>← Volver</button>
                <h1 className={styles.title}>Mis Compras</h1>
                <div style={{width: 80}}></div>
            </div>

            <div className={styles.content}>
                {loading ? (
                    <p className={styles.loading}>Cargando historial...</p>
                ) : history.length === 0 ? (
                    <div className={styles.emptyState}>
                        <p>Aún no has realizado compras.</p>
                    </div>
                ) : (
                    <div className={styles.list}>
                        {history.map((venta) => (
                            <div key={venta.id || venta._id} className={styles.card}>
                                <div className={styles.cardHeader}>
                                    <span className={styles.date}>
                                        {new Date(venta.fecha).toLocaleString()}
                                    </span>
                                    <span className={styles.total}>${venta.total}</span>
                                </div>
                                <div className={styles.itemsList}>
                                    {venta.items.map((item, idx) => (
                                        <div key={idx} className={styles.itemRow}>
                                            <span className={styles.prodName}>
                                                {latasMap[item.lataId || item.idLata] || "Producto"}
                                            </span>
                                            <span className={styles.qty}>x{item.cantidad}</span>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}