/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState } from "react";

const UserContext = createContext();
export const useUser = () => useContext(UserContext);

export function UserProvider({ children }) {
    const [user, setUser] = useState(null);

    const login = (name) => {
        setUser({ name, balance: 1000 });
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
