import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./components/Login/Login";
import MachineList from "./components/MachineList/MachineList";
import VendingMachine from "./components/VendingMachine/VendingMachine";
import HistoryPage from "./components/HistoryPage/HistoryPage";
import AdminDashboard from "./components/AdminPanel/AdminDashboard";
import SecretPage from "./components/SecretPage/SecretPage"; // <--- IMPORTAR
import ProtectedRoute from "./utils/ProtectedRoute";
import { useUser } from "./context/UserContext";

export default function App() {
    const { user } = useUser();

    return (
        <Routes>
            <Route path="/" element={!user ? <Login /> : <Navigate to={user.role === 'admin' ? "/admin" : "/home"} />} />

            {/* Rutas Clientes */}
            <Route path="/home" element={user ? <MachineList /> : <Navigate to="/" />} />
            <Route path="/machine/:id" element={user ? <VendingMachine /> : <Navigate to="/" />} />
            <Route path="/history" element={user ? <HistoryPage /> : <Navigate to="/" />} />

            {/* --- RUTA SECRETA (EASTER EGG) --- */}
            <Route path="/secret" element={user ? <SecretPage /> : <Navigate to="/" />} />

            {/* Ruta Admin */}
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