/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";
// Importamos nuestro nuevo servicio
import { getAllMachines, saveProductUpdate, createProduct } from "../services/machineService";

const VendingContext = createContext();
export const useVending = () => useContext(VendingContext);

export function VendingProvider({ children }) {
    // Estado inicial VACÍO (ahora esperamos que cargue del servicio)
    const [machines, setMachines] = useState([]);
    const [loading, setLoading] = useState(true);

    // 1. CARGA INICIAL (Simula GET /api/machines)
    useEffect(() => {
        const loadData = async () => {
            try {
                const data = await getAllMachines();
                setMachines(data);
                setLoading(false);
            } catch (error) {
                console.error("Error cargando máquinas", error);
                setLoading(false);
            }
        };
        loadData();
    }, []);

    // 2. DISMINUIR STOCK (Compra)
    const decreaseStock = (machineId, productId, qty = 1) => {
        // Actualizamos estado local visualmente (Optimistic UI)
        setMachines(prevMachines =>
            prevMachines.map(machine => {
                if (machine.id !== machineId) return machine;
                return {
                    ...machine,
                    products: machine.products.map(p =>
                        p.id === productId && p.stock >= qty
                            ? { ...p, stock: p.stock - qty }
                            : p
                    )
                };
            })
        );

        // NOTA: Aquí en el futuro llamarás a la API para avisar la venta
        // saveProductUpdate(machineId, ...);
    };

    // 3. ACTUALIZAR PRODUCTO (Admin)
    const updateProduct = async (machineId, productId, newStock, newPrice) => {
        // Primero actualizamos visualmente
        setMachines(prevMachines =>
            prevMachines.map(machine => {
                if (machine.id !== machineId) return machine;
                return {
                    ...machine,
                    products: machine.products.map(p =>
                        p.id === productId
                            ? { ...p, stock: Number(newStock), price: Number(newPrice) }
                            : p
                    )
                };
            })
        );

        // Llamamos al servicio (simulación de backend)
        await saveProductUpdate(machineId, { id: productId, stock: newStock, price: newPrice });
    };

    // 4. AGREGAR NUEVO PRODUCTO (Admin)
    const addNewProduct = async (machineId, productData) => {
        // Llamamos al servicio primero para simular la creación
        const newProductFromDB = await createProduct(machineId, productData);

        // Agregamos al estado local lo que "respondió" la base de datos
        setMachines(prevMachines =>
            prevMachines.map(machine => {
                if (machine.id !== machineId) return machine;

                return {
                    ...machine,
                    products: [
                        ...machine.products,
                        {
                            ...newProductFromDB,
                            image: newProductFromDB.image || "https://via.placeholder.com/150?text=Producto"
                        }
                    ]
                };
            })
        );
    };

    return (
        <VendingContext.Provider value={{ machines, decreaseStock, updateProduct, addNewProduct, loading }}>
            {/* Si está cargando, podrías mostrar un spinner, por ahora mostramos null o children directo */}
            {!loading && children}
        </VendingContext.Provider>
    );
}