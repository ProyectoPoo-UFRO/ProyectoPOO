import VendingMachine from "./components/VendingMachine/VendingMachine";
import Login from "./components/Login/Login";
import { useUser } from "./context/UserContext";

export default function App() {
    const { user } = useUser();

    return (
        <div>
            {/* Si no hay usuario logueado → mostrar Login */}
            {!user && <Login />}

            {/* Si está logueado → mostrar máquina */}
            {user && <VendingMachine />}
        </div>
    );
}
