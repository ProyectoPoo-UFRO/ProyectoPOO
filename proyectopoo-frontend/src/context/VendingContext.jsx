/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";
import { getAllMachines, saveProductUpdate, createProduct, deleteProduct, updateMachineStatus } from "../services/machineService";

const VendingContext = createContext();
export const useVending = () => useContext(VendingContext);

export function VendingProvider({ children }) {
    const [machines, setMachines] = useState([]);
    const [loading, setLoading] = useState(true);

    const refreshData = async () => {
        setLoading(true);
        try {
            const data = await getAllMachines();
            setMachines(data);
        } catch (error) {
            console.error("Error refrescando datos:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        refreshData();
    }, []);

    const changeMachineStatus = async (machineId, newStatus) => {
        const success = await updateMachineStatus(machineId, newStatus);

        if (success) {
            setMachines(prevMachines =>
                prevMachines.map(m =>
                    String(m.id) === String(machineId)
                        ? { ...m, status: newStatus }
                        : m
                )
            );
            return true;
        }
        return false;
    };

    const decreaseStock = (machineId, productId, qty = 1) => {
        setMachines(prevMachines =>
            prevMachines.map(machine => {
                if (String(machine.id) !== String(machineId)) return machine;

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
                if (String(machine.id) !== String(machineId)) return machine;
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

        if (!newProductFromDB) {
            console.error("❌ Fallo en Backend al crear producto");
            return false;
        }

        setMachines(prevMachines =>
            prevMachines.map(machine => {
                if (String(machine.id) !== String(machineId)) return machine;
                return {
                    ...machine,
                    products: [
                        ...machine.products,
                        {
                            ...newProductFromDB,
                            image: newProductFromDB.image || "/img/logo-can.png"
                        }
                    ]
                };
            })
        );
        return true;
    };

    const removeProduct = async (machineId, productId) => {
        setMachines(prevMachines =>
            prevMachines.map(machine => {
                if (String(machine.id) !== String(machineId)) return machine;
                return {
                    ...machine,
                    products: machine.products.filter(p => p.id !== productId)
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
                changeMachineStatus,
                loading,
                refreshData
            }}
        >
            {children}
        </VendingContext.Provider>
    );
}