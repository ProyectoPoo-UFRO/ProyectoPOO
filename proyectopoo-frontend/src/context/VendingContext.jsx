/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState } from "react";

const VendingContext = createContext();
export const useVending = () => useContext(VendingContext);

export function VendingProvider({ children }) {
    const [products, setProducts] = useState([
        { id: 1, name: "Coca-Cola", price: 1200, stock: 5 },
        { id: 2, name: "Pepsi", price: 1100, stock: 3 },
        { id: 3, name: "Agua", price: 800, stock: 10 },
        { id: 4, name: "Fanta", price: 1000, stock: 6 },
        { id: 5, name: "Sprite", price: 1000, stock: 7 },
    ]);

    const buyProduct = (productId) => {
        setProducts(prev =>
            prev.map(p =>
                p.id === productId && p.stock > 0
                    ? { ...p, stock: p.stock - 1 }
                    : p
            )
        );
    };

    return (
        <VendingContext.Provider value={{ products, buyProduct }}>
            {children}
        </VendingContext.Provider>
    );
}
