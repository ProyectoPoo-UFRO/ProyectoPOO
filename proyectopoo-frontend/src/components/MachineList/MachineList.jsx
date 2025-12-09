import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import styles from "./MachineList.module.css";
import Spinner from "../Spinner/Spinner";

const MACHINE_VARIANTS = [
    "/img/vm-1.png", "/img/vm-2.png", "/img/vm-3.png", "/img/vm-4.png",
    "/img/vm-5.png", "/img/vm-6.png", "/img/vm-7.png", "/img/vm-8.png"
];

export default function MachineList() {
    const { machines, loading } = useVending();
    const { user, logout, toggleMachineFavorite } = useUser(); // Importamos toggle
    const navigate = useNavigate();

    if (loading) return <Spinner />;

    const activateEasterEgg = () => navigate("/secret");

    const getStatusConfig = (status) => {
        switch (status) {
            case "OPERATIVA": return { text: "Operativa", style: styles.statusOperativa };
            case "FUERA_SERVICIO": return { text: "Fuera de Servicio", style: styles.statusFuera };
            case "EN_MANTENIMIENTO": return { text: "En Mantenimiento", style: styles.statusMantencion };
            case "SIN_STOCK": return { text: "Sin Stock", style: styles.statusSinStock };
            default: return { text: status || "Desconocido", style: styles.statusOperativa };
        }
    };

    const sortedMachines = [...machines].sort((a, b) => {
        const isFavA = user?.idMaquinasFavoritas?.includes(a.id) ? 1 : 0;
        const isFavB = user?.idMaquinasFavoritas?.includes(b.id) ? 1 : 0;
        return isFavB - isFavA;
    });

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <img src="/img/logo-can.png" alt="LatApp" className={styles.logoCorner} onClick={activateEasterEgg} />
                <div className={styles.headerCenter}>
                    <h2>Hola, <strong className={styles.userNameHighlight}>{user?.name}</strong></h2>
                    <span>Selecciona una máquina</span>
                </div>
                <div className={styles.headerActions}>
                    <button onClick={() => navigate("/history")} className={styles.historyButton}>📜 Mis Compras</button>
                    <button onClick={() => { logout(); navigate("/"); }} className={styles.logoutButton}>Cerrar sesión</button>
                </div>
            </div>

            <div className={styles.grid}>
                {sortedMachines.map((machine, index) => {
                    const statusConfig = getStatusConfig(machine.status);
                    const machineImage = MACHINE_VARIANTS[index % MACHINE_VARIANTS.length];
                    const isFavorite = user?.idMaquinasFavoritas?.includes(machine.id);

                    return (
                        <div key={machine.id} className={styles.card} onClick={() => navigate(`/machine/${machine.id}`)}>
                            <button
                                className={`${styles.favBtn} ${isFavorite ? styles.favBtnActive : ''}`}
                                onClick={(e) => { e.stopPropagation(); toggleMachineFavorite(machine.id); }}
                                title={isFavorite ? "Quitar de favoritas" : "Marcar como favorita"}
                            >
                                {isFavorite ? "❤️" : "🤍"}
                            </button>

                            <img src={machineImage} alt="Vending Machine" className={styles.machineImg} />
                            <h3 className={styles.machineName}>{machine.name}</h3>
                            <span className={`${styles.badge} ${statusConfig.style}`}>{statusConfig.text}</span>
                            <p className={styles.location}>{machine.location}</p>
                            <div className={styles.productCount}>{machine.products.length} productos disponibles</div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}