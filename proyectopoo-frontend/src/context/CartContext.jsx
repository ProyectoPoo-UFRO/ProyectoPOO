/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";

const CartContext = createContext();
export const useCart = () => useContext(CartContext);

export function CartProvider({ children }) {
    // 1. INICIALIZACIÓN: Leemos del localStorage o iniciamos con array vacío
    const [cart, setCart] = useState(() => {
        const savedCart = localStorage.getItem("vending_cart");
        return savedCart ? JSON.parse(savedCart) : [];
    });

    // 2. PERSISTENCIA: Guardamos cambios
    useEffect(() => {
        localStorage.setItem("vending_cart", JSON.stringify(cart));
    }, [cart]);

    // Agregar producto (Maneja lógica de aumentar cantidad si ya existe)
    const addToCart = (product) => {
        setCart(prevCart => {
            const existingItem = prevCart.find(item => item.id === product.id);

            if (existingItem) {
                // Si ya existe, sumamos 1 a la cantidad
                return prevCart.map(item =>
                    item.id === product.id
                        ? { ...item, quantity: item.quantity + 1 }
                        : item
                );
            } else {
                // Si es nuevo, lo agregamos con cantidad 1
                return [...prevCart, { ...product, quantity: 1 }];
            }
        });
    };

    const removeFromCart = (productId) => {
        setCart(prevCart => prevCart.filter(item => item.id !== productId));
    };

    const clearCart = () => {
        setCart([]);
    };

    return (
        <CartContext.Provider value={{ cart, addToCart, removeFromCart, clearCart }}>
            {children}
        </CartContext.Provider>
    );
}