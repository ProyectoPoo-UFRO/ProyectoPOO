/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState } from "react";

// Creamos el contexto
const VendingContext = createContext();

// Hook personalizado para usar el contexto
export const useVending = () => useContext(VendingContext);

// ESTE ES EL COMPONENTE "VendingProvider" QUE BUSCAS
export function VendingProvider({ children }) {

    // 1. Estado de MÁQUINAS (antes era solo productos)
    const [machines, setMachines] = useState([
        {
            id: 1,
            name: "Máquina Hall Central",
            location: "Edificio A - Piso 1",
            products: [
                { id: 101, name: "Coca-Cola", price: 1200, stock: 5 },
                { id: 102, name: "Pepsi", price: 1100, stock: 3 },
                { id: 103, name: "Agua Mineral", price: 800, stock: 10 }
            ]
        },
        {
            id: 2,
            name: "Máquina Cafetería",
            location: "Edificio B - Comedor",
            products: [
                { id: 201, name: "Súper 8", price: 500, stock: 20 },
                { id: 202, name: "Gullón", price: 1500, stock: 8 },
                { id: 203, name: "Jugo Naranja", price: 1000, stock: 0 }
            ]
        }
    ]);

    // Modificamos para aceptar una cantidad 'qty' (por defecto 1)
    const decreaseStock = (machineId, productId, qty = 1) => {
        setMachines(prevMachines =>
            prevMachines.map(machine => {
                if (machine.id !== machineId) return machine;

                return {
                    ...machine,
                    products: machine.products.map(p =>
                        p.id === productId && p.stock >= qty // Verificamos que haya stock suficiente
                            ? { ...p, stock: p.stock - qty }
                            : p
                    )
                };
            })
        );
    };

    // 3. Función para ADMIN: Actualizar precio y stock
    const updateProduct = (machineId, productId, newStock, newPrice) => {
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
    };

    // 4. Función para ADMIN: Agregar nuevo producto
    const addNewProduct = (machineId, productData) => {
        setMachines(prevMachines =>
            prevMachines.map(machine => {
                if (machine.id !== machineId) return machine;

                const newId = Math.floor(Math.random() * 100000); // ID temporal
                return {
                    ...machine,
                    products: [...machine.products, { id: newId, ...productData }]
                };
            })
        );
    };

    return (
        <VendingContext.Provider value={{ machines, decreaseStock, updateProduct, addNewProduct }}>
            {children}
        </VendingContext.Provider>
    );
}