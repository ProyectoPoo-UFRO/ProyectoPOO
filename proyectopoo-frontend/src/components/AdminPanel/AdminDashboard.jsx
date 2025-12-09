import { useState, useEffect } from "react";
import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import Spinner from "../Spinner/Spinner";
import styles from "./AdminDashboard.module.css";

export default function AdminDashboard() {
    const { machines, updateProduct, addNewProduct, removeProduct, changeMachineStatus, loading } = useVending();
    const { user, logout } = useUser();
    const navigate = useNavigate();

    const [selectedMachineId, setSelectedMachineId] = useState(null);
    const [newProdName, setNewProdName] = useState("");
    const [newProdPrice, setNewProdPrice] = useState("");
    const [newProdStock, setNewProdStock] = useState("");
    const [newProdImage, setNewProdImage] = useState("");
    const [notification, setNotification] = useState(null);
    const [productToDelete, setProductToDelete] = useState(null);

    useEffect(() => {
        if (machines.length > 0 && !selectedMachineId) {
            setSelectedMachineId(machines[0].id);
        }
    }, [machines, selectedMachineId]);

    if (loading) return <Spinner />;

    const activeMachineId = selectedMachineId || (machines.length > 0 ? machines[0].id : null);
    const currentMachine = machines.find(m => String(m.id) === String(activeMachineId));

    const showNotification = (message, type = 'success') => {
        setNotification({ message, type });
        setTimeout(() => setNotification(null), 3000);
    };

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    const handleStatusChange = async (e) => {
        const newStatus = e.target.value;
        if (!currentMachine) return;

        const success = await changeMachineStatus(currentMachine.id, newStatus);

        if (success) {
            showNotification(`Estado cambiado a: ${newStatus}`, "success");
        } else {
            showNotification("Error al cambiar estado", "error");
        }
    };

    const handleAddProduct = async (e) => {
        e.preventDefault();

        if(!newProdName.trim() || !newProdPrice || !newProdStock) {
            showNotification("Complete todos los campos obligatorios", "error");
            return;
        }

        const priceNum = Number(newProdPrice);
        const stockNum = Number(newProdStock);

        if (priceNum <= 0) { showNotification("El precio debe ser mayor a $0", "error"); return; }
        if (stockNum < 0) { showNotification("El stock no puede ser negativo", "error"); return; }

        if (!currentMachine) {
            showNotification("Error: No hay máquina seleccionada", "error");
            return;
        }

        const success = await addNewProduct(currentMachine.id, {
            name: newProdName,
            price: priceNum,
            stock: stockNum,
            image: newProdImage.trim()
        });

        if (success) {
            setNewProdName("");
            setNewProdPrice("");
            setNewProdStock("");
            setNewProdImage("");
            showNotification("Producto creado exitosamente", "success");
        } else {
            showNotification("Error al crear: Verifique conexión o datos", "error");
        }
    };

    const confirmDelete = () => {
        if (!productToDelete || !currentMachine) return;
        removeProduct(currentMachine.id, productToDelete.id);
        showNotification(`Producto eliminado: ${productToDelete.name}`, "success");
        setProductToDelete(null);
    };

    const getStatusColor = (status) => {
        const s = String(status).toUpperCase();
        if (s === 'OPERATIVA') return '#10b981';
        if (s === 'EN_MANTENIMIENTO') return '#f59e0b';
        return '#ef4444';
    };

    return (
        <div className={styles.container}>
            {productToDelete && (
                <div className={styles.modalOverlay}>
                    <div className={styles.modalContent}>
                        <h3 style={{marginTop:0, color:'#fff'}}>Confirmar Eliminación</h3>
                        <p style={{color:'#ccc'}}>
                            ¿Estás seguro de eliminar <strong>{productToDelete.name}</strong>?<br/>
                            Esta acción es irreversible.
                        </p>
                        <div className={styles.modalActions}>
                            <button className={styles.btnCancel} onClick={() => setProductToDelete(null)}>Cancelar</button>
                            <button className={styles.btnConfirm} onClick={confirmDelete}>Eliminar</button>
                        </div>
                    </div>
                </div>
            )}

            {notification && (
                <div className={`${styles.notification} ${styles[notification.type]}`}>
                    {notification.message}
                </div>
            )}

            <div className={styles.header}>
                <div className={styles.headerLeft}>
                    <h1 className={styles.title}>Panel de Administración</h1>
                </div>
                <div className={styles.headerCenter}>
                    <span className={styles.userName}>
                        Hola, <strong className={styles.userNameHighlight}>{user?.name}</strong>
                    </span>
                </div>
                <div className={styles.userActions}>
                    <button onClick={handleLogout} className={styles.logoutButton}>
                        Cerrar Sesión
                    </button>
                </div>
            </div>

            <div className={styles.controls}>
                <label>Máquina Activa:</label>
                <select
                    value={activeMachineId || ""}
                    onChange={(e) => setSelectedMachineId(e.target.value)}
                    className={styles.select}
                >
                    {machines.map(m => (
                        <option key={m.id} value={m.id}>
                            {m.name} — {m.location} (ID: {String(m.id).substring(0,6)}...)
                        </option>
                    ))}
                </select>
            </div>

            {currentMachine ? (
                <div className={styles.dashboardGrid}>
                    <div className={styles.sectionCard}>
                        <div style={{display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:'15px'}}>
                            <h2 className={styles.sectionTitle} style={{marginBottom:0}}>
                                Inventario: {currentMachine.name}
                            </h2>

                            <div style={{display:'flex', alignItems:'center', gap:'10px'}}>
                                <span style={{fontSize:'0.85rem', color:'var(--text-muted)', fontWeight:'bold'}}>ESTADO:</span>
                                <select
                                    value={currentMachine.status}
                                    onChange={handleStatusChange}
                                    className={styles.select}
                                    style={{
                                        width:'auto',
                                        padding:'5px 10px',
                                        fontSize:'0.85rem',
                                        borderColor: getStatusColor(currentMachine.status),
                                        color: getStatusColor(currentMachine.status),
                                        fontWeight: 'bold'
                                    }}
                                >
                                    <option value="OPERATIVA">🟢 Operativa</option>
                                    <option value="EN_MANTENIMIENTO">🟠 En Mantenimiento</option>

                                    <option value="FUERA_SERVICIO">🔴 Fuera de Servicio</option>
                                </select>
                            </div>
                        </div>

                        <div className={styles.tableContainer}>
                            <table className={styles.table}>
                                <thead>
                                <tr>
                                    <th>Img</th>
                                    <th>Producto</th>
                                    <th>Precio</th>
                                    <th>Stock</th>
                                    <th>Acciones</th>
                                </tr>
                                </thead>
                                <tbody>
                                {currentMachine.products.map(p => (
                                    <ProductRow
                                        key={p.id}
                                        product={p}
                                        machineId={currentMachine.id}
                                        updateProduct={updateProduct}
                                        onDeleteClick={() => setProductToDelete(p)}
                                        showNotification={showNotification}
                                        styles={styles}
                                    />
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div className={`${styles.sectionCard} ${styles.addProductForm}`}>
                        <h2 className={styles.sectionTitle}>+ Crear Producto</h2>
                        <form onSubmit={handleAddProduct} className={styles.formStack}>
                            <div className={styles.inputGroup}>
                                <label className={styles.inputLabel}>Nombre del Producto</label>
                                <input type="text" placeholder="Ej: Sprite Zero" value={newProdName} onChange={e => setNewProdName(e.target.value)} className={styles.select} />
                            </div>
                            <div className={styles.inputGroup}>
                                <label className={styles.inputLabel}>Precio ($)</label>
                                <input type="number" placeholder="1000" value={newProdPrice} onChange={e => setNewProdPrice(e.target.value)} className={styles.select} />
                            </div>
                            <div className={styles.inputGroup}>
                                <label className={styles.inputLabel}>Stock Inicial</label>
                                <input type="number" placeholder="10" value={newProdStock} onChange={e => setNewProdStock(e.target.value)} className={styles.select} />
                            </div>
                            <div className={styles.inputGroup}>
                                <label className={styles.inputLabel}>Imagen URL (Opcional)</label>
                                <input type="text" placeholder="https://..." value={newProdImage} onChange={e => setNewProdImage(e.target.value)} className={styles.select} />
                            </div>
                            <button type="submit" className={styles.addButton}>Guardar Producto</button>
                        </form>
                    </div>
                </div>
            ) : (
                <div style={{ padding: 40, textAlign: "center", color: "#888" }}>
                    <p>No se encontró información de la máquina seleccionada.</p>
                </div>
            )}
        </div>
    );
}

function ProductRow({ product, machineId, updateProduct, onDeleteClick, showNotification, styles }) {
    const [price, setPrice] = useState(product.price);
    const [stock, setStock] = useState(product.stock);

    const handleSave = () => {
        const priceNum = Number(price);
        const stockNum = Number(stock);
        if (priceNum <= 0) { showNotification("El precio debe ser positivo", "error"); setPrice(product.price); return; }
        if (stockNum < 0) { showNotification("El stock no puede ser negativo", "error"); setStock(product.stock); return; }
        updateProduct(machineId, product.id, stockNum, priceNum);
        showNotification("Cambios guardados", "success");
    };

    return (
        <tr>
            <td style={{ width: '50px' }}>
                <img
                    src={product.image || "https://via.placeholder.com/40"}
                    alt="ico"
                    onError={(e) => { e.target.src = "https://via.placeholder.com/40?text=?"; }}
                    style={{ width: '40px', height: '40px', objectFit: 'contain', borderRadius: '4px', background: '#fff' }}
                />
            </td>
            <td><strong>{product.name}</strong></td>
            <td><input type="number" value={price} onChange={(e) => setPrice(e.target.value)} className={styles.inputEdit} /></td>
            <td><input type="number" value={stock} onChange={(e) => setStock(e.target.value)} className={styles.inputEdit} /></td>
            <td>
                <div className={styles.actionButtons}>
                    <button onClick={handleSave} className={`${styles.iconBtn} ${styles.btnSave}`} title="Guardar">💾</button>
                    <button onClick={onDeleteClick} className={`${styles.iconBtn} ${styles.btnDelete}`} title="Eliminar">🗑️</button>
                </div>
            </td>
        </tr>
    );
}