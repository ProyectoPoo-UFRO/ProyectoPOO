import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import styles from "./MachineList.module.css";
import Spinner from "../Spinner/Spinner";

export default function MachineList() {
    const { machines, loading } = useVending();
    const { user, logout } = useUser();
    const navigate = useNavigate();

    if (loading) return <Spinner />;

    // Función secreta del Easter Egg
    const activateEasterEgg = () => {
        navigate("/secret");
    };

    return (
        <div className={styles.container}>
            <div className={styles.header}>

                {/* 1. IZQUIERDA: LOGO (CON CLICK SECRETO) */}
                <img
                    src="/img/logo-can.png"
                    alt="LatApp"
                    className={styles.logoCorner}
                    onClick={activateEasterEgg} // <--- MANTENEMOS EL GATILLO
                    title="LatApp"
                />

                {/* 2. CENTRO: SALUDO DESTACADO (RESTAURADO) */}
                <div className={styles.headerCenter}>
                    <h2>
                        Hola, <strong className={styles.userNameHighlight}>{user?.name}</strong>
                    </h2>
                    <span>Selecciona una máquina</span>
                </div>

                {/* 3. DERECHA: BOTONES */}
                <div className={styles.headerActions}>
                    <button
                        onClick={() => navigate("/history")}
                        className={styles.historyButton}
                    >
                        📜 Mis Compras
                    </button>

                    <button
                        onClick={() => { logout(); navigate("/"); }}
                        className={styles.logoutButton}
                    >
                        Cerrar sesión
                    </button>
                </div>
            </div>

            <div className={styles.grid}>
                {machines.map(machine => (
                    <div
                        key={machine.id}
                        onClick={() => navigate(`/machine/${machine.id}`)}
                        className={styles.card}
                    >
                        <div className={styles.machineIcon}>🏢</div>
                        <h3 className={styles.machineName}>{machine.name}</h3>
                        <p className={styles.location}>{machine.location}</p>
                        <span className={styles.status}>
                            {machine.products.length} productos
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
}