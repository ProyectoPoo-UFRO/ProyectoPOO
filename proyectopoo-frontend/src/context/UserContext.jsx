/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";

const UserContext = createContext();
export const useUser = () => useContext(UserContext);

const API_URL = "http://localhost:8081/api/v1";

const parseJwt = (token) => {
    try {
        if (!token) return null;
        const cleanToken = token.trim().replace(/^"|"$/g, '');
        const base64Url = cleanToken.split('.')[1];
        if (!base64Url) return null;
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
};

export function UserProvider({ children }) {
    const [user, setUser] = useState(() => {
        const savedUser = localStorage.getItem("vending_user");
        return savedUser ? JSON.parse(savedUser) : null;
    });

    useEffect(() => {
        if (user) {
            localStorage.setItem("vending_user", JSON.stringify(user));
        } else {
            localStorage.removeItem("vending_user");
        }
    }, [user]);

    useEffect(() => {
        const refreshUserData = async () => {
            if (user?.token && user?.id) {
                try {
                    const cleanToken = user.token.toString().trim().replace(/^"|"$/g, '');
                    const response = await fetch(`${API_URL}/usuarios/${user.id}`, {
                        headers: {
                            'Authorization': `Bearer ${cleanToken}`,
                            'Content-Type': 'application/json'
                        }
                    });

                    if (response.ok) {
                        const userData = await response.json();
                        setUser(prev => ({
                            ...prev,
                            balance: userData.saldo,
                            name: userData.nombre,
                            idLatasFavoritas: userData.idLatasFavoritas || [],
                            idMaquinasFavoritas: userData.idMaquinasFavoritas || []
                        }));
                    }
                } catch (error) {
                    console.error("Error refreshing user:", error);
                }
            }
        };
        refreshUserData();
    }, []);

    const login = async (username, password) => {
        try {
            const authResponse = await fetch(`${API_URL}/auth/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ nombre: username, contrasenia: password })
            });

            if (authResponse.ok) {
                let rawToken = await authResponse.text();
                const token = rawToken.toString().trim().replace(/^"|"$/g, '');

                const decodedToken = parseJwt(token);
                const realId = decodedToken?.sub || decodedToken?.userId || decodedToken?.id;

                if (!realId) return false;

                let realBalance = 0;
                let realName = username;
                let favLatas = [];
                let favMaquinas = [];

                try {
                    const userDetailsResponse = await fetch(`${API_URL}/usuarios/${realId}`, {
                        headers: { 'Authorization': `Bearer ${token}` }
                    });

                    if (userDetailsResponse.ok) {
                        const userDetails = await userDetailsResponse.json();
                        realBalance = userDetails.saldo;
                        realName = userDetails.nombre;
                        favLatas = userDetails.idLatasFavoritas || [];
                        favMaquinas = userDetails.idMaquinasFavoritas || [];
                    }
                } catch (e) {
                    console.warn("⚠️ Error obteniendo detalles adicionales");
                }

                const role = username.toLowerCase().includes("admin") ? "admin" : "client";

                setUser({
                    id: realId,
                    name: realName,
                    role: role,
                    balance: realBalance,
                    idLatasFavoritas: favLatas,
                    idMaquinasFavoritas: favMaquinas,
                    token: token
                });

                return true;
            }
        } catch (error) {
            console.error("Error conectando login:", error);
        }
        return false;
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem("vending_user");
    };

    const deductBalance = (amount) => {
        if (!user) return;
        setUser(prev => ({ ...prev, balance: prev.balance - amount }));
    };

    const addPurchase = () => {};

    const toggleMachineFavorite = async (machineId) => {
        if (!user) return;
        const isFav = user.idMaquinasFavoritas.includes(machineId);
        const newFavorites = isFav
            ? user.idMaquinasFavoritas.filter(id => id !== machineId)
            : [...user.idMaquinasFavoritas, machineId];

        setUser(prev => ({ ...prev, idMaquinasFavoritas: newFavorites }));

        try {
            const method = isFav ? 'DELETE' : 'POST';
            const cleanToken = user.token.toString().trim().replace(/^"|"$/g, '');
            await fetch(`${API_URL}/usuarios/${user.id}/favoritas/maquinas/${machineId}`, {
                method: method,
                headers: { 'Authorization': `Bearer ${cleanToken}` }
            });
        } catch (e) {
            console.error("Error fav maquina", e);
        }
    };

    const toggleProductFavorite = async (lataId) => {
        if (!user) return;
        const isFav = user.idLatasFavoritas.includes(lataId);
        const newFavorites = isFav
            ? user.idLatasFavoritas.filter(id => id !== lataId)
            : [...user.idLatasFavoritas, lataId];

        setUser(prev => ({ ...prev, idLatasFavoritas: newFavorites }));

        try {
            const method = isFav ? 'DELETE' : 'POST';
            const cleanToken = user.token.toString().trim().replace(/^"|"$/g, '');
            await fetch(`${API_URL}/usuarios/${user.id}/favoritas/latas/${lataId}`, {
                method: method,
                headers: { 'Authorization': `Bearer ${cleanToken}` }
            });
        } catch (e) {
            console.error("Error fav lata", e);
        }
    };

    return (
        <UserContext.Provider value={{
            user, login, logout, deductBalance, addPurchase,
            toggleMachineFavorite, toggleProductFavorite
        }}>
            {children}
        </UserContext.Provider>
    );
}