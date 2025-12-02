import { useState } from "react";
import { useUser } from "../../context/UserContext";

export default function Login() {
    const { login } = useUser();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function handleSubmit(e) {
        e.preventDefault();

        const ok = login(username, password);

        if (!ok) {
            alert("Usuario o contraseña incorrectos");
        }
    }

    return (
        <div style={{ padding: "20px" }}>
            <h1>Login</h1>

            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Usuario"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <br /><br />

                <input
                    type="password"
                    placeholder="Contraseña"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <br /><br />

                <button type="submit">Ingresar</button>
            </form>
        </div>
    );
}
