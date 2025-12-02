import { Navigate } from "react-router-dom";
import { useUser } from "../context/UserContext";

export default function ProtectedRoute({ children }) {
    const { user } = useUser();

    // 1. Si no hay usuario logueado -> Al Login
    if (!user) {
        return <Navigate to="/" replace />;
    }

    // 2. Si hay usuario pero NO es admin -> Al Home de clientes
    if (user.role !== 'admin') {
        return <Navigate to="/home" replace />;
    }

    // 3. Si pasa las validaciones, muestra el contenido protegido
    return children;
}