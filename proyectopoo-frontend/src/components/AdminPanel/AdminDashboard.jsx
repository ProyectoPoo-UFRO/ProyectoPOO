import { useState } from "react";
import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import Spinner from "../Spinner/Spinner";
import styles from "./AdminDashboard.module.css";

export default function AdminDashboard() {
    const { machines, updateProduct, addNewProduct, removeProduct, loading } = useVending();
    const { user, logout } = useUser();
    const navigate = useNavigate();

    const [selectedMachineId, setSelectedMachineId] = useState(null);
    const [newProdName, setNewProdName] = useState("");
    const [newProdPrice, setNewProdPrice] = useState("");
    const [newProdStock, setNewProdStock] = useState("");
    const [newProdImage, setNewProdImage] = useState("");
    const [notification, setNotification] = useState(null);
    const [productToDelete, setProductToDelete] = useState(null);

    if (loading) return <Spinner />;

    const activeMachineId = selectedMachineId ?? (machines.length > 0 ? machines[0].id : null);
    const currentMachine = machines.find(m => m.id === Number(activeMachineId));

    const showNotification = (message, type = 'success') => {
        setNotification({ message, type });
        setTimeout(() => setNotification(null), 3000);
    };

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    const handleAddProduct = (e) => {
        e.preventDefault();

        if(!newProdName.trim() || !newProdPrice || !newProdStock) {
            showNotification("Complete todos los campos obligatorios", "error");
            return;
        }

        const priceNum = Number(newProdPrice);
        const stockNum = Number(newProdStock);

        if (priceNum <= 0) {
            showNotification("El precio debe ser mayor a $0", "error");
            return;
        }

        if (stockNum < 0) {
            showNotification("El stock no puede ser negativo", "error");
            return;
        }

        if (!currentMachine) return;

        addNewProduct(currentMachine.id, {
            name: newProdName,
            price: priceNum,
            stock: stockNum,
            image: newProdImage.trim()
        });

        setNewProdName("");
        setNewProdPrice("");
        setNewProdStock("");
        setNewProdImage("");

        showNotification("Producto creado exitosamente", "success");
    };

    const confirmDelete = () => {
        if (!productToDelete || !currentMachine) return;
        removeProduct(currentMachine.id, productToDelete.id);
        showNotification(`Producto eliminado: ${productToDelete.name}`, "success");
        setProductToDelete(null);
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
                <h1 className={styles.title}>Panel de Administración</h1>

                <div className={styles.userActions}>
                    <span className={styles.userName}>
                        Hola, <strong>{user?.name}</strong>
                    </span>
                    <button onClick={handleLogout} className={styles.logoutButton}>
                        Cerrar Sesión
                    </button>
                </div>
            </div>

            <div className={styles.controls}>
                <label>Máquina Activa:</label>
                <select
                    value={activeMachineId || ""}
                    onChange={(e) => setSelectedMachineId(Number(e.target.value))}
                    className={styles.select}
                >
                    {machines.map(m => (
                        <option key={m.id} value={m.id}>
                            {m.name} — {m.location}
                        </option>
                    ))}
                </select>
            </div>

            {currentMachine ? (
                <div className={styles.dashboardGrid}>

                    <div className={styles.sectionCard}>
                        <h2 className={styles.sectionTitle}>Inventario: {currentMachine.name}</h2>
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
                                <input
                                    type="text"
                                    placeholder="Ej: Sprite Zero"
                                    value={newProdName}
                                    onChange={e => setNewProdName(e.target.value)}
                                    className={styles.select}
                                />
                            </div>

                            <div className={styles.inputGroup}>
                                <label className={styles.inputLabel}>Precio ($)</label>
                                <input
                                    type="number"
                                    placeholder="1000"
                                    value={newProdPrice}
                                    onChange={e => setNewProdPrice(e.target.value)}
                                    className={styles.select}
                                />
                            </div>

                            <div className={styles.inputGroup}>
                                <label className={styles.inputLabel}>Stock Inicial</label>
                                <input
                                    type="number"
                                    placeholder="10"
                                    value={newProdStock}
                                    onChange={e => setNewProdStock(e.target.value)}
                                    className={styles.select}
                                />
                            </div>

                            <div className={styles.inputGroup}>
                                <label className={styles.inputLabel}>Imagen URL (Opcional)</label>
                                <input
                                    type="text"
                                    placeholder="https://..."
                                    value={newProdImage}
                                    onChange={e => setNewProdImage(e.target.value)}
                                    className={styles.select}
                                />
                            </div>

                            <button type="submit" className={styles.addButton}>Guardar Producto</button>
                        </form>
                    </div>

                </div>
            ) : (
                <div style={{ padding: 40, textAlign: "center", color: "#888" }}>
                    <p>No se encontró información de la máquina.</p>
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

        if (priceNum <= 0) {
            showNotification("El precio debe ser positivo", "error");
            setPrice(product.price); return;
        }
        if (stockNum < 0) {
            showNotification("El stock no puede ser negativo", "error");
            setStock(product.stock); return;
        }
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
            <td>
                <input type="number" value={price} onChange={(e) => setPrice(e.target.value)} className={styles.inputEdit} />
            </td>
            <td>
                <input type="number" value={stock} onChange={(e) => setStock(e.target.value)} className={styles.inputEdit} />
            </td>
            <td>
                <div className={styles.actionButtons}>
                    <button onClick={handleSave} className={`${styles.iconBtn} ${styles.btnSave}`} title="Guardar">💾</button>
                    <button onClick={onDeleteClick} className={`${styles.iconBtn} ${styles.btnDelete}`} title="Eliminar">🗑️</button>
                </div>
            </td>
        </tr>
    );
}