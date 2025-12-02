import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import styles from "./MachineList.module.css"; // Importamos los estilos

export default function MachineList() {
    const { machines } = useVending();
    const { user, logout } = useUser();
    const navigate = useNavigate();

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <div>
                    <h2>Hola, {user?.name}</h2>
                    <span style={{color: '#888'}}>Selecciona una máquina para comprar</span>
                </div>
                <button
                    onClick={() => { logout(); navigate("/"); }}
                    className={styles.logoutButton}
                >
                    Cerrar sesión
                </button>
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