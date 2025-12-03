/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";

const UserContext = createContext();
export const useUser = () => useContext(UserContext);

export function UserProvider({ children }) {
    const [user, setUser] = useState(() => {
        const savedUser = localStorage.getItem("vending_user");
        return savedUser ? JSON.parse(savedUser) : null;
    });

    const [history, setHistory] = useState(() => {
        const savedHistory = localStorage.getItem("vending_history");
        return savedHistory ? JSON.parse(savedHistory) : [];
    });

    useEffect(() => {
        if (user) {
            localStorage.setItem("vending_user", JSON.stringify(user));
        } else {
            localStorage.removeItem("vending_user");
        }
    }, [user]);

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
    };

    const deductBalance = (amount) => {
        if (!user) return;
        setUser(prev => ({ ...prev, balance: prev.balance - amount }));
    };

    const addPurchase = (cartItems, totalAmount, machineName) => {
        const newRecord = {
            id: Date.now(),
            date: new Date().toLocaleString(),
            machine: machineName,
            items: cartItems,
            total: totalAmount
        };

        setHistory(prev => [newRecord, ...prev]);
    };

    return (
        <UserContext.Provider value={{ user, login, logout, deductBalance, history, addPurchase }}>
            {children}
        </UserContext.Provider>
    );
}