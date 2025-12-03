import { useState } from "react";
import { useUser } from "../../context/UserContext";
import styles from "./Login.module.css";

export default function Login() {
    const { login } = useUser();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    function handleSubmit(e) {
        e.preventDefault();
        setError("");

        if (!username.trim() || !password.trim()) {
            setError("Por favor complete todos los campos.");
            return;
        }

        const isSuccess = login(username, password);

        if (!isSuccess) {
            setError("Usuario o contraseña incorrectos.");
        }
    }

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <h1 className={styles.title}>Iniciar Sesión</h1>

                <form onSubmit={handleSubmit}>
                    <div className={styles.formGroup}>
                        <label className={styles.label}>Usuario</label>
                        <input
                            className={styles.input}
                            type="text"
                            placeholder="Ingrese su usuario"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>

                    <div className={styles.formGroup}>
                        <label className={styles.label}>Contraseña</label>
                        <input
                            className={styles.input}
                            type="password"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>

                    {error && <div className={styles.errorMessage}>⚠️ {error}</div>}

                    <button type="submit" className={styles.button}>
                        Ingresar
                    </button>
                </form>
            </div>
        </div>
    );
}