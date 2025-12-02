import { useState } from "react";
import { useVending } from "../../context/VendingContext";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import styles from "./AdminDashboard.module.css";

export default function AdminDashboard() {
    const { machines, updateProduct, addNewProduct } = useVending();
    const { user, logout } = useUser();
    const navigate = useNavigate();

    // Estado para saber qué máquina se está editando
    const [selectedMachineId, setSelectedMachineId] = useState(machines[0]?.id || null);

    // Estados para el formulario de nuevo producto
    const [newProdName, setNewProdName] = useState("");
    const [newProdPrice, setNewProdPrice] = useState("");
    const [newProdStock, setNewProdStock] = useState("");
    const [newProdImage, setNewProdImage] = useState(""); // <--- NUEVO ESTADO PARA IMAGEN

    // Estado para notificaciones (Toast)
    const [notification, setNotification] = useState(null);

    const currentMachine = machines.find(m => m.id === Number(selectedMachineId));

    // Función auxiliar para mostrar notificaciones
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

        // Validación (La imagen es opcional, si está vacía el Context pone un placeholder)
        if(!newProdName.trim() || !newProdPrice || !newProdStock) {
            showNotification("Por favor complete nombre, precio y stock", "error");
            return;
        }

        addNewProduct(currentMachine.id, {
            name: newProdName,
            price: Number(newProdPrice),
            stock: Number(newProdStock),
            image: newProdImage.trim() // <--- ENVIAMOS LA IMAGEN
        });

        // Limpiar inputs
        setNewProdName("");
        setNewProdPrice("");
        setNewProdStock("");
        setNewProdImage(""); // <--- LIMPIAMOS EL CAMPO IMAGEN

        showNotification("Producto agregado correctamente", "success");
    };

    return (
        <div className={styles.container}>
            {/* Renderizado de Notificación Flotante */}
            {notification && (
                <div className={`${styles.notification} ${styles[notification.type]}`}>
                    {notification.type === 'success' ? '✔' : '❌'} {notification.message}
                </div>
            )}

            {/* Cabecera */}
            <div className={styles.header}>
                <h1 className={styles.title}>⚙️ Panel de Control</h1>
                <div className={styles.userInfo}>
                    <span>Admin: <strong>{user?.name}</strong></span>
                    <button onClick={handleLogout} className={styles.logoutButton}>
                        Cerrar Sesión
                    </button>
                </div>
            </div>

            {/* Selector de Máquina */}
            <div className={styles.controls}>
                <label><strong>Gestionar Máquina: </strong></label>
                <select
                    value={selectedMachineId}
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

            {/* Contenido Principal */}
            {currentMachine ? (
                <>
                    <h2 style={{ marginBottom: '15px' }}>Inventario: {currentMachine.name}</h2>

                    {/* Tabla de Productos */}
                    <div className={styles.tableContainer}>
                        <table className={styles.table}>
                            <thead>
                            <tr>
                                <th>Img</th> {/* Nueva columna opcional para ver foto pequeñita si quieres */}
                                <th>Producto</th>
                                <th>Precio ($)</th>
                                <th>Stock</th>
                                <th>Acción</th>
                            </tr>
                            </thead>
                            <tbody>
                            {currentMachine.products.map(p => (
                                <ProductRow
                                    key={p.id}
                                    product={p}
                                    machineId={currentMachine.id}
                                    updateProduct={updateProduct}
                                    showNotification={showNotification}
                                    styles={styles}
                                />
                            ))}
                            </tbody>
                        </table>
                    </div>

                    {/* Formulario para Agregar */}
                    <div className={styles.addProductForm}>
                        <h3 style={{ marginTop: 0, marginBottom: '15px' }}>➕ Nuevo Producto</h3>
                        <form onSubmit={handleAddProduct} className={styles.formRow}>

                            {/* Input Nombre */}
                            <div className={styles.inputGroup}>
                                <span className={styles.inputLabel}>Nombre</span>
                                <input
                                    type="text"
                                    placeholder="Ej: Sprite"
                                    value={newProdName}
                                    onChange={e => setNewProdName(e.target.value)}
                                    className={styles.select}
                                    style={{ width: '180px', marginLeft: 0 }}
                                />
                            </div>

                            {/* Input Precio */}
                            <div className={styles.inputGroup}>
                                <span className={styles.inputLabel}>Precio</span>
                                <input
                                    type="number"
                                    placeholder="1000"
                                    value={newProdPrice}
                                    onChange={e => setNewProdPrice(e.target.value)}
                                    className={styles.select}
                                    style={{ width: '90px', marginLeft: 0 }}
                                />
                            </div>

                            {/* Input Stock */}
                            <div className={styles.inputGroup}>
                                <span className={styles.inputLabel}>Stock</span>
                                <input
                                    type="number"
                                    placeholder="10"
                                    value={newProdStock}
                                    onChange={e => setNewProdStock(e.target.value)}
                                    className={styles.select}
                                    style={{ width: '70px', marginLeft: 0 }}
                                />
                            </div>

                            {/* NUEVO: Input Imagen URL */}
                            <div className={styles.inputGroup}>
                                <span className={styles.inputLabel}>URL Imagen (Opcional)</span>
                                <input
                                    type="text"
                                    placeholder="https://..."
                                    value={newProdImage}
                                    onChange={e => setNewProdImage(e.target.value)}
                                    className={styles.select}
                                    style={{ width: '250px', marginLeft: 0 }}
                                />
                            </div>

                            <button type="submit" className={styles.addButton}>Agregar</button>
                        </form>
                    </div>
                </>
            ) : (
                <p>No se encontró información de la máquina.</p>
            )}
        </div>
    );
}

// Subcomponente de Fila
function ProductRow({ product, machineId, updateProduct, showNotification, styles }) {
    const [price, setPrice] = useState(product.price);
    const [stock, setStock] = useState(product.stock);

    const handleSave = () => {
        updateProduct(machineId, product.id, stock, price);
        showNotification("Producto actualizado", "success");
    };

    return (
        <tr>
            {/* Previsualización pequeña de la imagen */}
            <td style={{ width: '50px' }}>
                <img
                    src={product.image || "https://via.placeholder.com/40"}
                    alt="mini"
                    style={{ width: '40px', height: '40px', objectFit: 'contain', borderRadius: '4px', backgroundColor: '#fff' }}
                />
            </td>
            <td>{product.name}</td>
            <td>
                <input
                    type="number"
                    value={price}
                    onChange={(e) => setPrice(e.target.value)}
                    className={styles.inputEdit}
                />
            </td>
            <td>
                <input
                    type="number"
                    value={stock}
                    onChange={(e) => setStock(e.target.value)}
                    className={styles.inputEdit}
                />
            </td>
            <td>
                <button onClick={handleSave} className={styles.saveButton}>
                    💾 Guardar
                </button>
            </td>
        </tr>
    );
}