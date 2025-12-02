/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState } from "react";

const UserContext = createContext();
export const useUser = () => useContext(UserContext);

export function UserProvider({ children }) {
    const [user, setUser] = useState(null);

    const login = (username, password) => {
        // --- SIMULACIÓN DE BACKEND ---
        // Aquí conectarás tu API Java más adelante.

        // Caso 1: Administrador (Hardcodeado para pruebas)
        if (username === "admin" && password === "1234") {
            setUser({
                name: "Administrador",
                role: "admin", // <--- Importante para diferenciar vistas
                balance: 999999999
            });
            return true; // Login exitoso
        }

        // Caso 2: Cliente Normal (Cualquier otro usuario con contraseña 'user')
        if (password === "user") {
            setUser({
                name: username,
                role: "client",
                balance: 10000 // Saldo inicial cliente
            });
            return true; // Login exitoso
        }

        // Caso 3: Credenciales incorrectas
        return false;
    };

    const logout = () => setUser(null);

    const deductBalance = (amount) => {
        if (!user) return;
        setUser(prev => ({ ...prev, balance: prev.balance - amount }));
    };

    return (
        <UserContext.Provider value={{ user, login, logout, deductBalance }}>
            {children}
        </UserContext.Provider>
    );
}