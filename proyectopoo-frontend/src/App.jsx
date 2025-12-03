import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./components/Login/Login";
import MachineList from "./components/MachineList/MachineList";
import VendingMachine from "./components/VendingMachine/VendingMachine";
import HistoryPage from "./components/HistoryPage/HistoryPage";
import AdminDashboard from "./components/AdminPanel/AdminDashboard";
import ProtectedRoute from "./utils/ProtectedRoute"; // Asegúrate de que esta ruta sea correcta
import { useUser } from "./context/UserContext";

export default function App() {
    const { user } = useUser();

    return (
        <Routes>
            {/* Ruta Raíz: Login o Redirección */}
            <Route path="/" element={!user ? <Login /> : <Navigate to={user.role === 'admin' ? "/admin" : "/home"} />} />

            {/* Rutas de Cliente */}
            <Route path="/home" element={user ? <MachineList /> : <Navigate to="/" />} />

            {/* Ruta Dinámica para abrir una máquina específica */}
            <Route path="/machine/:id" element={user ? <VendingMachine /> : <Navigate to="/" />} />

            <Route path="/history" element={user ? <HistoryPage /> : <Navigate to="/" />} />

            {/* Ruta de Administrador */}
            <Route
                path="/admin"
                element={
                    <ProtectedRoute>
                        <AdminDashboard />
                    </ProtectedRoute>
                }
            />

            {/* Cualquier otra ruta lleva al inicio */}
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
}