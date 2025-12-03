/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";
import { getAllMachines, saveProductUpdate, createProduct, deleteProduct } from "../services/machineService";

const VendingContext = createContext();
export const useVending = () => useContext(VendingContext);

export function VendingProvider({ children }) {
    const [machines, setMachines] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadData = async () => {
            try {
                const data = await getAllMachines();
                setMachines(data);
            } catch (error) {
                console.error("Error cargando máquinas", error);
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, []);

    const decreaseStock = (machineId, productId, qty = 1) => {
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
    };

    const updateProduct = async (machineId, productId, newStock, newPrice) => {
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
        await saveProductUpdate(machineId, { id: productId, stock: newStock, price: newPrice });
    };

    const addNewProduct = async (machineId, productData) => {
        const newProductFromDB = await createProduct(machineId, productData);

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

    const removeProduct = async (machineId, productId) => {
        setMachines(prevMachines =>
            prevMachines.map(machine => {
                if (machine.id !== machineId) return machine;
                return {
                    ...machine,
                    products: machine.products.filter(p => p.id !== productId) // Filtramos el eliminado
                };
            })
        );

        await deleteProduct(machineId, productId);
    };

    return (
        <VendingContext.Provider
            value={{
                machines,
                decreaseStock,
                updateProduct,
                addNewProduct,
                removeProduct,
                loading
            }}
        >
            {children}
        </VendingContext.Provider>
    );
}