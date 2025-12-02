/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";

const UserContext = createContext();
export const useUser = () => useContext(UserContext);

export function UserProvider({ children }) {
    // 1. CARGAR USUARIO
    const [user, setUser] = useState(() => {
        const savedUser = localStorage.getItem("vending_user");
        return savedUser ? JSON.parse(savedUser) : null;
    });

    // 2. CARGAR HISTORIAL (Nuevo)
    const [history, setHistory] = useState(() => {
        const savedHistory = localStorage.getItem("vending_history");
        return savedHistory ? JSON.parse(savedHistory) : [];
    });

    // Persistencia del Usuario
    useEffect(() => {
        if (user) {
            localStorage.setItem("vending_user", JSON.stringify(user));
        } else {
            localStorage.removeItem("vending_user");
        }
    }, [user]);

    // Persistencia del Historial (Nuevo)
    useEffect(() => {
        localStorage.setItem("vending_history", JSON.stringify(history));
    }, [history]);

    const login = (username, password) => {
        if (username === "admin" && password === "1234") {
            setUser({ name: "Administrador", role: "admin", balance: 999999999 });
            return true;
        }
        if (password === "user") {
            setUser({ name: username, role: "client", balance: 10000 });
            return true;
        }
        return false;
    };

    const logout = () => {
        setUser(null);
        // Opcional: ¿Quieres borrar el historial al salir?
        // Si quieres que sea persistente entre sesiones, comenta la siguiente línea:
        // setHistory([]);
    };

    const deductBalance = (amount) => {
        if (!user) return;
        setUser(prev => ({ ...prev, balance: prev.balance - amount }));
    };

    // NUEVA FUNCIÓN: Registrar compra
    const addPurchase = (cartItems, totalAmount, machineName) => {
        const newRecord = {
            id: Date.now(), // ID único basado en tiempo
            date: new Date().toLocaleString(), // Fecha y hora legible
            machine: machineName,
            items: cartItems, // Guardamos una copia del carrito
            total: totalAmount
        };

        // Agregamos al principio del array para que lo más nuevo salga primero
        setHistory(prev => [newRecord, ...prev]);
    };

    return (
        <UserContext.Provider value={{ user, login, logout, deductBalance, history, addPurchase }}>
            {children}
        </UserContext.Provider>
    );
}