import { Routes, Route, Navigate } from "react-router-dom";
import VendingMachine from "./components/VendingMachine/VendingMachine";
import MachineList from "./components/MachineList/MachineList";
import Login from "./components/Login/Login";
import AdminDashboard from "./components/AdminPanel/AdminDashboard"; // Importar
import ProtectedRoute from "./utils/ProtectedRoute";     // Importar
import { useUser } from "./context/UserContext";

export default function App() {
    const { user } = useUser();

    return (
        <Routes>
            <Route path="/" element={!user ? <Login /> : <Navigate to={user.role === 'admin' ? "/admin" : "/home"} />} />

            {/* Rutas Clientes */}
            <Route path="/home" element={user ? <MachineList /> : <Navigate to="/" />} />
            <Route path="/machine/:id" element={user ? <VendingMachine /> : <Navigate to="/" />} />

            {/* RUTA PROTEGIDA DE ADMINISTRADOR */}
            <Route
                path="/admin"
                element={
                    <ProtectedRoute>
                        <AdminDashboard />
                    </ProtectedRoute>
                }
            />

            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
}